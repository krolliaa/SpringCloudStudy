package cn.itcast.account.tcc;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

@LocalTCC
public interface AccountTCCService {
    @TwoPhaseBusinessAction(name = "deduct", commitMethod = "confirm", rollbackMethod = "cancel")
    public abstract void deduct(@BusinessActionContextParameter(paramName = "userId") String userId, @BusinessActionContextParameter(paramName = "money") int money);
    public abstract boolean confirm(BusinessActionContext businessActionContext);
    public abstract boolean cancel(BusinessActionContext businessActionContext);
}
