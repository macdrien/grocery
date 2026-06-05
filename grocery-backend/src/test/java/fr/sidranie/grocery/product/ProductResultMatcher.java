package fr.sidranie.grocery.product;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.ArrayList;
import java.util.List;

import org.springframework.test.web.servlet.ResultMatcher;

public class ProductResultMatcher {

    public static ResultMatcher[] matchesProduct(String jsonPathPrefix, Product expected) {
        return new ResultMatcher[]{
                jsonPath(jsonPathPrefix + ".id").value(expected.getId()),
                jsonPath(jsonPathPrefix + ".slug").value(expected.getSlug().getValue()),
                jsonPath(jsonPathPrefix + ".name").value(expected.getName().getValue()),
                jsonPath(jsonPathPrefix + ".price").value(expected.getPrice().getValue()),
        };
    }

    public static ResultMatcher[] matchesProducts(String jsonRootPrefix, List<Product> products) {
        List<ResultMatcher> matchers = new ArrayList<>();
        for (int index = 0; index < products.size(); index++) {
            matchers.addAll(List.of(matchesProduct(String.format("%s[%d]", jsonRootPrefix, index),
                                                   products.get(index))));
        }
        return matchers.toArray(ResultMatcher[]::new);
    }
}
