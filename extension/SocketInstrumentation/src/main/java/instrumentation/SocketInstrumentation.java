package instrumentation;

import static net.bytebuddy.matcher.ElementMatchers.named;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.logging.Logger;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import io.opentelemetry.javaagent.extension.instrumentation.TypeInstrumentation;
import io.opentelemetry.javaagent.extension.instrumentation.TypeTransformer;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

public class SocketInstrumentation implements TypeInstrumentation {

	private static Logger logger = Logger.getLogger(SocketInstrumentation.class.getName());

	@Override
	public ElementMatcher<TypeDescription> typeMatcher() {
		logger.info("Entering typeMatcher");	
		return ElementMatchers.named("java.net.Socket");
	}

	@Override
	public void transform(TypeTransformer typeTransformer) {
		logger.info("Entering transform...");
//		typeTransformer.applyAdviceToMethod(named("connect"), this.getClass().getName() + "$SocketPoolAdvice");

//		typeTransformer.applyAdviceToMethod(
//				isConstructor().and(takesArgument(0, named("java.lang.String"))).and(takesArguments(2)).and(isPublic()),
//				this.getClass().getName() + "$ConstructorAdvice");

		typeTransformer.applyAdviceToMethod(named("connect"), this.getClass().getName() + "$ConnectAdvice");

//		typeTransformer.applyAdviceToMethod(named("connect").and(takesArguments(1)),
//				"SocketInstrumentation$ConnectAdvice");

		typeTransformer.applyAdviceToMethod(named("getInputStream"),
				this.getClass().getName() + "$GetInputStreamAdvice");

		typeTransformer.applyAdviceToMethod(named("getOutputStream"),
				this.getClass().getName() + "$GetOutputStreamAdvice");
		
//		typeTransformer.applyAdviceToMethod(named("getOutputStream"),
//				this.getClass().getName() + "$CloseAdvice");
	}

	/*
	 * public static class ConstructorAdvice {
	 * 
	 * @Advice.OnMethodEnter(suppress = Throwable.class) public static Scope
	 * enter(@Advice.Argument(0) SocketAddress socketAddress, @Advice.Argument(0)
	 * String host,
	 * 
	 * @Advice.Argument(0) int port, @Advice.Local("otelSpan") Span span) {
	 * 
	 * System.out.println("Entering method in ConstructorAdvice");
	 * 
	 * Tracer tracer =
	 * GlobalOpenTelemetry.getTracer("java.net.socket.instrumentation"); span =
	 * tracer.spanBuilder("java.net.Socket.create").startSpan();
	 * 
	 * span.setAttribute("net.peer.ip", host); span.setAttribute("net.peer.port",
	 * port); span.setAttribute("net.transport", "ip_tcp");
	 * 
	 * Scope scope = span.makeCurrent(); return scope; }
	 * 
	 * @Advice.OnMethodExit(onThrowable = Throwable.class) public static void
	 * exit(@Advice.Thrown Throwable throwable, @Advice.Local("otelSpan") Span span,
	 * 
	 * @Advice.Enter Scope scope) {
	 * 
	 * System.out.println("Exiting method in ConstructorAdvice");
	 * 
	 * if (span != null) { if (throwable != null) {
	 * span.setStatus(StatusCode.ERROR); span.recordException(throwable); } else {
	 * span.setStatus(StatusCode.OK); } span.end(); }
	 * 
	 * if (scope != null) { scope.close(); } } }
	 */
	public static class ConnectAdvice {

		@Advice.OnMethodEnter(suppress = Throwable.class)
		public static Scope enter(@Advice.Argument(0) SocketAddress socketAddress,
				@Advice.Local("otelSpan") Span span) {

//			System.out.println("Entering method in ConnectAdvice");

			Tracer tracer = GlobalOpenTelemetry.getTracer("java.net.socket.instrumentation");
			span = tracer.spanBuilder("java.net.Socket.connect").startSpan();

			if (socketAddress instanceof InetSocketAddress) {
				InetSocketAddress inetSocketAddress = (InetSocketAddress) socketAddress;
				span.setAttribute("net.peer.ip", inetSocketAddress.getAddress().getHostAddress());
				span.setAttribute("net.peer.port", inetSocketAddress.getPort());
				span.setAttribute("net.transport", "ip_tcp");
			}

			Scope scope = span.makeCurrent();
			return scope;
		}

		@Advice.OnMethodExit(onThrowable = Throwable.class)
		public static void exit(@Advice.Thrown Throwable throwable, @Advice.Local("otelSpan") Span span,
				@Advice.Enter Scope scope) {

//			System.out.println("Exiting method in ConnectAdvice");

			if (span != null) {
				if (throwable != null) {
					span.setStatus(StatusCode.ERROR);
					span.recordException(throwable);
				} else {
					span.setStatus(StatusCode.OK);
				}
				span.end();
			}

			if (scope != null) {
				scope.close();
			}
		}
	}

	public static class GetInputStreamAdvice {

		@Advice.OnMethodEnter(suppress = Throwable.class)
		public static Scope enter(@Advice.Local("otelSpan") Span span) {

//			System.out.println("Entering method in GetInputStreamAdvice");
			Tracer tracer = GlobalOpenTelemetry.getTracer("java.net.socket.instrumentation");
			span = tracer.spanBuilder("java.net.Socket.getInputStream").startSpan();

			Scope scope = span.makeCurrent();
			return scope;
		}

		@Advice.OnMethodExit(onThrowable = Throwable.class)
		public static void exit(@Advice.Thrown Throwable throwable, @Advice.Local("otelSpan") Span span,
				@Advice.Enter Scope scope) {

//			System.out.println("Exiting method in GetInputStreamAdvice");

			if (span != null) {
				if (throwable != null) {
					span.setStatus(StatusCode.ERROR);
					span.recordException(throwable);
				} else {
					span.setStatus(StatusCode.OK);
				}
				span.end();
			}

			if (scope != null) {
				scope.close();
			}
		}
	}

	public static class GetOutputStreamAdvice {

		@Advice.OnMethodEnter(suppress = Throwable.class)
		public static Scope enter(@Advice.Local("otelSpan") Span span) {

//			System.out.println("Entering method in GetOutputStreamAdvice");

			Tracer tracer = GlobalOpenTelemetry.getTracer("java.net.socket.instrumentation");
			span = tracer.spanBuilder("java.net.Socket.getOutputStream").startSpan();

			Scope scope = span.makeCurrent();
			return scope;
		}

		@Advice.OnMethodExit(onThrowable = Throwable.class)
		public static void exit(@Advice.Thrown Throwable throwable, @Advice.Local("otelSpan") Span span,
				@Advice.Enter Scope scope) {

//			System.out.println("Exiting method in GetOutputStreamAdvice");

			if (span != null) {
				if (throwable != null) {
					span.setStatus(StatusCode.ERROR);
					span.recordException(throwable);
				} else {
					span.setStatus(StatusCode.OK);
				}
				span.end();
			}

			if (scope != null) {
				scope.close();
			}
		}
	}
	
	public static class CloseAdvice {

		@Advice.OnMethodEnter(suppress = Throwable.class)
		public static Scope enter(@Advice.Local("otelSpan") Span span) {

//			System.out.println("Entering method in CloseAdvice");
			Tracer tracer = GlobalOpenTelemetry.getTracer("java.net.socket.instrumentation");
			span = tracer.spanBuilder("java.net.Socket.close").startSpan();

			Scope scope = span.makeCurrent();
			return scope;
		}

		@Advice.OnMethodExit(onThrowable = Throwable.class)
		public static void exit(@Advice.Thrown Throwable throwable, @Advice.Local("otelSpan") Span span,
				@Advice.Enter Scope scope) {

//			System.out.println("Exiting method in CloseAdvice");

			if (span != null) {
				if (throwable != null) {
					span.setStatus(StatusCode.ERROR);
					span.recordException(throwable);
				} else {
					span.setStatus(StatusCode.OK);
				}
				span.end();
			}

			if (scope != null) {
				scope.close();
			}
		}
	}
	/*
	 * public static class SocketPoolAdvice {
	 * 
	 * @Advice.OnMethodEnter(suppress = Throwable.class) public static Scope
	 * onEnter(@Advice.Local("otelSpan") Span span) {
	 * 
	 * System.out.println("Entering method");
	 * 
	 * Tracer tracer =
	 * GlobalOpenTelemetry.getTracer("java.net.socket.instrumentation", "1.0.0");
	 * 
	 * span = tracer.spanBuilder("mySpan").startSpan();
	 * 
	 * Scope scope = span.makeCurrent();
	 * 
	 * return scope;
	 * 
	 * }
	 * 
	 * @Advice.OnMethodExit(onThrowable = Throwable.class, suppress =
	 * Throwable.class) public static void onExit(@Advice.Thrown Throwable
	 * throwable, @Advice.Local("otelSpan") Span span,
	 * 
	 * @Advice.Enter Scope scope) { if (scope != null) { scope.close(); }
	 * 
	 * if (throwable != null) { span.setStatus(StatusCode.ERROR,
	 * "Exception thrown in method"); }
	 * 
	 * if (span != null) { span.end(); } } }
	 */
}
