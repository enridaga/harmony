package harmony.tools.serializers;

import java.io.IOException;
import java.io.OutputStream;

public interface Serializer<I> {
	
	public void setInput(I input);
	
	public void writeTo(OutputStream output) throws IOException;
}
