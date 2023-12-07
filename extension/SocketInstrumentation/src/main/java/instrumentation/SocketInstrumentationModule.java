package instrumentation;

import static java.util.Collections.singletonList;

import java.util.List;
import java.util.logging.Logger;

import com.google.auto.service.AutoService;

import io.opentelemetry.javaagent.extension.instrumentation.InstrumentationModule;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;

@AutoService(InstrumentationModule.class)
public final class SocketInstrumentationModule extends InstrumentationModule {
	
	private static Logger logger = Logger.getLogger(SocketInstrumentationModule.class.getName());

	public SocketInstrumentationModule() {
		super("SocketInstrumentation");
		logger.info("SocketInstrumentationModule activated....");
	}

	@Override
	public List<TypeInstrumentation> typeInstrumentations() {
		return singletonList(new SocketInstrumentation());
	}

	@Override
	public boolean isHelperClass(String className) {
		return className.matches("java.net.Socket");
	}

}
