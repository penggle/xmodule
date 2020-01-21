package com.penglecode.xmodule.springboot.examples.validator.consts;

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
	
	public interface Property1 {}
	
	public interface Property2 {}

	public interface Property3 {}
	
	public interface Property4 {}
	
	public interface Property5 {}
	
	public interface Property6 {}
	
	public interface Property7 {}
	
	public interface Property8 {}
	
	public interface Property9 {}
	
	public interface Property10 {}
	
	public interface Property11 {}
	
	public interface Property12 {}

	public interface Property13 {}
	
	public interface Property14 {}
	
	public interface Property15 {}
	
	public interface Property16 {}
	
	public interface Property17 {}
	
	public interface Property18 {}
	
	public interface Property19 {}
	
	public interface Property20 {}
	
	public interface Property21 {}
	
	public interface Property22 {}

	public interface Property23 {}
	
	public interface Property24 {}
	
	public interface Property25 {}
	
	public interface Property26 {}
	
	public interface Property27 {}
	
	public interface Property28 {}
	
	public interface Property29 {}
	
	public interface Property30 {}
	
	@GroupSequence({Property1.class, Property2.class, Property3.class, Property4.class, Property5.class, Property6.class, Property7.class, Property8.class, Property9.class, Property10.class,
		Property11.class, Property12.class, Property13.class, Property14.class, Property15.class, Property16.class, Property17.class, Property18.class, Property19.class, Property20.class,
		Property21.class, Property22.class, Property23.class, Property24.class, Property25.class, Property26.class, Property27.class, Property28.class, Property29.class, Property30.class})
	public interface PropertyOrder {
		
	}
	
	//同一个属性字段上的验证顺序组定义
	
	public interface Constraint1 {}
	
	public interface Constraint2 {}

	public interface Constraint3 {}
	
	public interface Constraint4 {}
	
	public interface Constraint5 {}
	
	
	@GroupSequence({Constraint1.class, Constraint2.class, Constraint3.class, Constraint4.class, Constraint5.class})
	public interface ConstraintOrder {
		
	}
	
	@GroupSequence({Default.class, PropertyOrder.class, ConstraintOrder.class})
	public interface ValidationOrder {
		
	}
	
}
