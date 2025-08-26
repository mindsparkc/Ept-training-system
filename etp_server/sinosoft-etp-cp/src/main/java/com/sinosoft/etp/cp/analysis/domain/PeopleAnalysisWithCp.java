package com.sinosoft.etp.cp.analysis.domain;

import com.sinosoft.common.core.domain.BaseEntity;

public class PeopleAnalysisWithCp extends BaseEntity {
    private String name;

    private String type;

    private String barWidth;

    private Double[] data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(String barWidth) {
        this.barWidth = barWidth;
    }

    public Double[] getData() {
        return data;
    }

    public void setData(Double[] data) {
        this.data = data;
    }
}
