package com.penglecode.xmodule.common.validation.validator;

import javax.validation.GroupSequence;

public class ValidationGroups {

	//验证所属操作组定义
	
	public interface Create {}
	
	public interface Update {}

	public interface Delete {}
	
	public interface Remove {}
	
	public interface Enable {}
	
	public interface Disable {}
	
	public interface UpdatePwd {}
	
	public interface ResetPwd {}

	public interface UpdateState {}
	
	public interface UpdateFlag {}
	
	public interface UpdateType {}
	
	public interface TurnOn {}
	
	public interface TurnOff {}
	
	public interface Online {}
	
	public interface Offline {}
	
	public interface Reset {}


	public interface Order1 {}

	public interface Order2 {}

	public interface Order3 {}

	@GroupSequence({Order1.class, Order2.class, Order3.class})
	public interface OrderSequence {

	}

}
