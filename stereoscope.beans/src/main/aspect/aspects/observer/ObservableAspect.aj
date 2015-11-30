package aspects.observer;

import org.aspectj.lang.JoinPoint;

import com.stereokrauts.lib.logging.StereoscopeLogManager;

public aspect ObservableAspect perthis(setterMethods(IAspectedObservable)) {
	
	pointcut setterMethods(IAspectedObservable observed) :
		execution(public * model.beans.*.set*(..))
		&& this(observed)
		&& this(IAspectedObservable);
	
	private Object oldValue = null;
	
	before(final IAspectedObservable observed) : setterMethods(observed) {
		this.oldValue = this.getValueOf(observed, thisJoinPoint);
	}
	
	after(final IAspectedObservable observed) : setterMethods(observed) {
		final Object newValue = this.getValueOf(observed, thisJoinPoint);
		String beanValueName = thisJoinPoint.getSignature().getName().replaceFirst("set", "");
		beanValueName = beanValueName.substring(0, 1).toLowerCase() + beanValueName.substring(1);
		StereoscopeLogManager.getLogger(ObservableAspect.class).info("The value of " + observed + "[" + beanValueName + "] was changed from " + this.oldValue + " to " + newValue);
		observed.getObserverManager().notifyObservers(observed, beanValueName, this.oldValue, newValue);
	}
	
	private Object getValueOf(Object observed, JoinPoint joinPoint) {
		String getterMethodName = joinPoint.getSignature().getName().replaceFirst("set", "get");
		try {
			return observed.getClass().getMethod(getterMethodName, new Class<?>[0]).invoke(observed, new Object[0]);
		} catch (Exception e) {
			StereoscopeLogManager.getLogger(ObservableAspect.class).info("Error getting old value of object " + observed);
		}
		return null;
	}

}
