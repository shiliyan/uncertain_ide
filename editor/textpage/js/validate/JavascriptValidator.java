package editor.textpage.js.validate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;

public class JavascriptValidator {

	private ErrorReporter errorReporter;

	public class WarningReporter {
		public int character;

		public String evidence;

		public int line;

		public String reason;
	}

	public JavascriptValidator(ErrorReporter errorReporter) {
		this.errorReporter = errorReporter;
	}

	public void validate(String name, String source) {
		Context cx = Context.enter();
		Scriptable scope = cx.initStandardObjects();
		cx.setErrorReporter(errorReporter);
		try {
			cx.compileString(source, name, 0, null);
			WarningReporter warningReporter = new WarningReporter();
			scope.put("meReport", scope, warningReporter);
			scope.put("scriptText", scope, source);
			Object eval = this.getScript().exec(cx, scope);
			if ("$ME_JS_ERROR".equals(eval)) {
				String reason = warningReporter.reason.trim();
				if (reason == null
						|| !"Expected an identifier and instead saw 'debugger'." //$NON-NLS-1$
						.equals(reason)) {
					((AnnotationReporter) errorReporter).warning(
							warningReporter.reason, name,
							warningReporter.line - 1, warningReporter.evidence,
							warningReporter.character);
				}
			} else if (eval instanceof String) {
				// success
			}
		}catch (EvaluatorException e2) {
			// that is ok; @see org.mozilla.javascript.EvaluatorException
		} 
		catch (IOException e) {
			//js/fulljslint.js ;
		} finally {
			Context.exit();
		}
	}

	private static Script fulljslintScript;

	public final Script getScript() throws IOException {
		if (fulljslintScript == null) {
			InputStream resourceAsStream = null;
			Context cx = Context.enter();
			try {
				resourceAsStream = this.getClass().getClassLoader()
						.getResourceAsStream("js/fulljslint.js"); 
				InputStreamReader osw = new InputStreamReader(resourceAsStream);
				fulljslintScript = cx.compileReader(osw, "JS lint", 1, null); 
			} finally {
				Context.exit();
				if (resourceAsStream != null)
					resourceAsStream.close();
			}
		}
		return fulljslintScript;
	}
}
