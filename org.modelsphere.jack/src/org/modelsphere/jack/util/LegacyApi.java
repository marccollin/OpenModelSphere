package org.modelsphere.jack.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.StringTokenizer;

/** Replacements for APIs deprecated since Java 20, keeping their lenient behavior. */
public final class LegacyApi {

    private LegacyApi() {
    }

    /** Like the deprecated new URL(String), but strict parsing is tried first. */
    @SuppressWarnings("deprecation")
    public static URL url(String spec) throws MalformedURLException {
        try {
            return URI.create(spec).toURL();
        } catch (IllegalArgumentException | MalformedURLException e) {
            // spec is not a valid URI (spaces, backslashes, ...): fall back to lenient parsing
            return new URL(spec);
        }
    }

    /** Same tokenization as the deprecated Runtime.exec(String). */
    public static Process exec(Runtime runtime, String command) throws IOException {
        StringTokenizer st = new StringTokenizer(command);
        String[] cmdarray = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++) {
            cmdarray[i] = st.nextToken();
        }
        return runtime.exec(cmdarray);
    }
}
