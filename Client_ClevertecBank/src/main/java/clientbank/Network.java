package clientbank;

public class Network<I, O> {
    private final I inputStream;
    private final O outputStream;

    public Network(I inputStream, O outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public I getInputStream() {
        return inputStream;
    }

    public O getOutputStream() {
        return outputStream;
    }
}
