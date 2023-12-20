package com.decas.reasoner.repository;


import com.decas.reasoner.dto.SosaIndividual;
import lombok.Getter;
import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

@Getter
public class QueryResult<T extends SosaIndividual> {
    private final String[] vars;
    private final Object[][] rows;

    public QueryResult(List<String> vars, List<List<String>> rows) {
        this.vars = vars.toArray(new String[vars.size()]);
        this.rows = new Object[rows.size()][];

        for (int i = 0; i < vars.size(); i++) {
            this.vars[i] = WordUtils.capitalize(this.vars[i].replace("_", " "));
        };

        for (int i = 0; i < rows.size(); i++) {
            List<String> innerList = rows.get(i);
            this.rows[i] = innerList.toArray(new Object[0]);
        };
    }

    public String getTextResult() {
        if (rows.length == 0) {
            return "false";
        }

        return (String) rows[0][0];
    }
}
