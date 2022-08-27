package cn.itcast.account.tcc.impl;

import cn.itcast.account.entity.AccountFreeze;
import cn.itcast.account.mapper.AccountFreezeMapper;
import cn.itcast.account.mapper.AccountMapper;
import cn.itcast.account.tcc.AccountTCCService;
import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AccountTCCServiceImpl implements AccountTCCService {

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountFreezeMapper accountFreezeMapper;

    @Override
    @Transactional
    public void deduct(String userId, int money) {
        //获取当前事务的 id
        String xid = RootContext.getXID();
        //避免业务悬挂，查询 account_freeze 表中，看有无该 xid 已经存在了，如果存在即不为 null 则表示这是一个空回滚不需要继续执行 Try 操作
        if (accountFreezeMapper.selectById(xid) != null) return;
        //1.扣除可用余额
        accountMapper.deduct(userId, money);
        //2.记录冻结余额
        AccountFreeze accountFreeze = new AccountFreeze();
        accountFreeze.setXid(xid);
        accountFreeze.setUserId(userId);
        accountFreeze.setFreezeMoney(money);
        accountFreeze.setState(AccountFreeze.State.TRY);
        accountFreezeMapper.insert(accountFreeze);
    }

    @Override
    public boolean confirm(BusinessActionContext businessActionContext) {
        //confirm 阶段就是删除掉冻结余额
        //获取事务 id
        String xid = businessActionContext.getXid();
        //删除该事务
        int count = accountFreezeMapper.deleteById(xid);
        //判断 confirm 是否执行正常
        return count == 1;
    }

    @Override
    public boolean cancel(BusinessActionContext businessActionContext) {
        String xid = businessActionContext.getXid();
        //恢复可用余额
        AccountFreeze accountFreeze = accountFreezeMapper.selectById(xid);
        //幂等性，如果为CANCEL表示已经做过了
        if (accountFreeze.getState() == AccountFreeze.State.CANCEL) return true;
        //如果这是一个超时的操作，TC 会让该事务直接进入 Cancel 段，所以需要先判断下是不是空的
        //RM 会在注册分支事务之前该分支就会被 TM 所调用，进行检查测试，所以如果查到的数据为 null 说明该分支事务被阻塞无法访问
        //此时直接进行空回滚
        if (accountFreeze == null) {
            accountFreeze = new AccountFreeze();
            String userId = businessActionContext.getActionContext("userId").toString();
            accountFreeze.setUserId(userId);
            accountFreeze.setFreezeMoney(0);
            accountFreeze.setState(AccountFreeze.State.CANCEL);
            accountFreeze.setXid(xid);
            accountFreezeMapper.insert(accountFreeze);
            return true;
        }
        accountMapper.refund(accountFreeze.getUserId(), accountFreeze.getFreezeMoney());//后台已经做好可用+冻结 = 恢复
        //修改冻结余额，更改状态为 Cancel
        accountFreeze.setFreezeMoney(0);
        accountFreeze.setState(AccountFreeze.State.CANCEL);
        int count = accountFreezeMapper.updateById(accountFreeze);
        return count == 1;
    }
}