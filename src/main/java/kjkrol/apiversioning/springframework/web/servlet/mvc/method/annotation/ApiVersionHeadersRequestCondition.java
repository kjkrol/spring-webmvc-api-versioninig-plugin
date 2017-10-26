package kjkrol.apiversioning.springframework.web.servlet.mvc.method.annotation;

import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.condition.AbstractRequestCondition;
import org.springframework.web.servlet.mvc.condition.HeadersRequestCondition;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

import static java.util.Objects.nonNull;
import static kjkrol.apiversioning.springframework.web.servlet.mvc.method.annotation.ApiVersionHeader.X_API_VERSION;

class ApiVersionHeadersRequestCondition extends AbstractRequestCondition<ApiVersionHeadersRequestCondition> {

    private final HeadersRequestCondition headersRequestCondition;

    ApiVersionHeadersRequestCondition(String... headers) {
        headersRequestCondition = new HeadersRequestCondition(headers);
    }

    private ApiVersionHeadersRequestCondition(HeadersRequestCondition headersRequestCondition) {
        this.headersRequestCondition = headersRequestCondition;
    }

    @Override
    protected Collection<?> getContent() {
        return headersRequestCondition.getExpressions();
    }

    @Override
    protected String getToStringInfix() {
        return " && ";
    }

    @Override
    public ApiVersionHeadersRequestCondition combine(ApiVersionHeadersRequestCondition other) {
        HeadersRequestCondition newHeadersRequestCondition = headersRequestCondition.combine(other.headersRequestCondition);
        return new ApiVersionHeadersRequestCondition(newHeadersRequestCondition);
    }

    @Nullable
    @Override
    public ApiVersionHeadersRequestCondition getMatchingCondition(HttpServletRequest request) {
        HeadersRequestCondition headersRequestCondition = this.headersRequestCondition.getMatchingCondition(request);
        if (nonNull(request.getHeader(X_API_VERSION)) && getContent().isEmpty()) {
            return null;
        }
        if (nonNull(headersRequestCondition)) {
            return this;
        }
        return null;
    }

    @Override
    public int compareTo(ApiVersionHeadersRequestCondition other, HttpServletRequest request) {
        return this.headersRequestCondition.compareTo(other.headersRequestCondition, request);
    }
}
