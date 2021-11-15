package nuparu.ni;

import nuparu.ni.exception.EvaluationErrorException;

public interface IChainable {

	boolean hasValue();
	Value evaluate() throws EvaluationErrorException;
}
