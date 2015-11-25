package example.unit.matchers;

import com.google.common.primitives.Bytes;
import example.Edge;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class EdgeMatcher extends BaseMatcher<Edge> {

    private Byte[] expected;
    private boolean reverse;

    public EdgeMatcher(Edge expectedEdge, boolean shouldCompareReversed) {

        List<Byte> points = Bytes.asList(expectedEdge.asArray());
        if (shouldCompareReversed) {
            Collections.reverse(points);
        }
        this.expected = points.toArray(new Byte[points.size()]);

        this.reverse = shouldCompareReversed;
    }

    public boolean matches(Object o) {
        return o != null && Arrays.equals(expected, collectPoints((Edge) o));
    }

    @Override
	public void describeMismatch(Object item, Description description) {
        description.appendText("was ").appendValue(Arrays.toString(collectPoints((Edge) item)));
    }

    public void describeTo(Description description) {
        description.appendText(Arrays.toString(expected));
    }

    private Byte[] collectPoints(Edge edge) {

        List<Byte> points = new ArrayList<>();
        Iterator<Byte> iter = reverse? edge.getPointsReverse() : edge.getPoints();
        while (iter.hasNext()) {
            points.add(iter.next());
        }

        return points.toArray(new Byte[points.size()]);
    }
}
