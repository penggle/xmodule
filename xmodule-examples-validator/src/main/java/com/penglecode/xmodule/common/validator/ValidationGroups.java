package com.penglecode.xmodule.common.validator;

import javax.validation.GroupSequence;
import javax.validation.groups.Default;

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
	
	//同一个属性字段上的验证顺序组定义
	
	public interface Constraint1 {}
	
	public interface Constraint2 {}

	public interface Constraint3 {}
	
	public interface Constraint4 {}
	
	public interface Constraint5 {}
	
	@GroupSequence({Default.class, Create.class, Update.class, Constraint1.class, Constraint2.class, Constraint3.class, Constraint4.class, Constraint5.class})
	public interface ConstraintOrder {
		
	}
	
}
