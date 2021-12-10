package org.job.interview.tests.extractors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class PaginationRetriver {

    static int pagesCount(String linksHeader) {
        if (linksHeader == null) {
            return 1;
        }
        return extractIndexFromLink(linksHeader);
    }

    private static int extractIndexFromLink(String last) {
        Pattern r = Pattern.compile("=(\\d+)>; rel=\"last\"");
        Matcher matcher = r.matcher(last);
        if (!matcher.find()) {
            throw new IllegalStateException(String.format("Illegal format of link: '%s'", last));
        }
        return Integer.parseInt(matcher.group(1));
    }
}
