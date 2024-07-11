import java.util.logging.Logger;

class ComplexNumber {
    private double real;
    private double imaginary;

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }

    @Override
    public String toString() {
        return real + " + " + imaginary + "i";
    }
}

interface ComplexOperation {
    ComplexNumber add(ComplexNumber a, ComplexNumber b);
    ComplexNumber multiply(ComplexNumber a, ComplexNumber b);
    ComplexNumber divide(ComplexNumber a, ComplexNumber b);
}

class ComplexCalculator implements ComplexOperation {
    @Override
    public ComplexNumber add(ComplexNumber a, ComplexNumber b) {
        return new ComplexNumber(a.getReal() + b.getReal(), a.getImaginary() + b.getImaginary());
    }

    @Override
    public ComplexNumber multiply(ComplexNumber a, ComplexNumber b) {
        double real = a.getReal() * b.getReal() - a.getImaginary() * b.getImaginary();
        double imaginary = a.getReal() * b.getImaginary() + a.getImaginary() * b.getReal();
        return new ComplexNumber(real, imaginary);
    }

    @Override
    public ComplexNumber divide(ComplexNumber a, ComplexNumber b) {
        double denominator = b.getReal() * b.getReal() + b.getImaginary() * b.getImaginary();
        double real = (a.getReal() * b.getReal() + a.getImaginary() * b.getImaginary()) / denominator;
        double imaginary = (a.getImaginary() * b.getReal() - a.getReal() * b.getImaginary()) / denominator;
        return new ComplexNumber(real, imaginary);
    }
}

class ComplexCalculatorDecorator implements ComplexOperation {
    protected ComplexOperation decoratedCalculator;

    public ComplexCalculatorDecorator(ComplexOperation decoratedCalculator) {
        this.decoratedCalculator = decoratedCalculator;
    }

    @Override
    public ComplexNumber add(ComplexNumber a, ComplexNumber b) {
        return decoratedCalculator.add(a, b);
    }

    @Override
    public ComplexNumber multiply(ComplexNumber a, ComplexNumber b) {
        return decoratedCalculator.multiply(a, b);
    }

    @Override
    public ComplexNumber divide(ComplexNumber a, ComplexNumber b) {
        return decoratedCalculator.divide(a, b);
    }
}

class LoggingComplexCalculatorDecorator extends ComplexCalculatorDecorator {
    private static final Logger logger = Logger.getLogger(LoggingComplexCalculatorDecorator.class.getName());

    public LoggingComplexCalculatorDecorator(ComplexOperation decoratedCalculator) {
        super(decoratedCalculator);
    }

    @Override
    public ComplexNumber add(ComplexNumber a, ComplexNumber b) {
        ComplexNumber result = super.add(a, b);
        logger.info("Adding " + a + " and " + b + ": " + result);
        return result;
    }

    @Override
    public ComplexNumber multiply(ComplexNumber a, ComplexNumber b) {
        ComplexNumber result = super.multiply(a, b);
        logger.info("Multiplying " + a + " and " + b + ": " + result);
        return result;
    }

    @Override
    public ComplexNumber divide(ComplexNumber a, ComplexNumber b) {
        ComplexNumber result = super.divide(a, b);
        logger.info("Dividing " + a + " by " + b + ": " + result);
        return result;
    }
}

class ComplexCalculatorFactory {
    public static ComplexOperation createCalculator() {
        return new LoggingComplexCalculatorDecorator(new ComplexCalculator());
    }

    public static ComplexOperation createLoggingDecorator(ComplexOperation calculator) {
        return new LoggingComplexCalculatorDecorator(calculator);
    }
}

public class Main {
    public static void main(String[] args) {
        ComplexOperation calculator = ComplexCalculatorFactory.createCalculator();

        ComplexNumber a = new ComplexNumber(1, 2);
        ComplexNumber b = new ComplexNumber(3, 4);

        System.out.println("Addition: " + calculator.add(a, b));
        System.out.println("Multiplication: " + calculator.multiply(a, b));
        System.out.println("Division: " + calculator.divide(a, b));
    }
}
