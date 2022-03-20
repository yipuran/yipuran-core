package org.yipuran.function;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	AcceptFunctionTest.class,
	ApplyConsumerTest.class,
	NullableFunctionTest.class,
	PredicateFunctionTest.class,
	ThrowableConsumerTest.class,
	ThrowableFunctionTest.class,
	ThrowablePredicateTest.class,
	ThrowableSupplierTest.class,
	ThrowableUnaryOperatorTest.class,
	ThrowableBiConsumerTest.class,
	ReturnalConsumerTest.class,
})

public class FunctionTest {

}
