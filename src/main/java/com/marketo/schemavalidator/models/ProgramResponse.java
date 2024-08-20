package com.marketo.schemavalidator.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.ZonedDateTime;
import java.util.List;

public class ProgramResponse {
    private Integer id;
    private String name;
    private String description;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    private String url;
    private String type;
    private String channel;
    private Folder folder;
    private String status;
    private String workspace;
    private List<Tags> tags;
    private List<Costs> costs;

    private ZonedDateTime startDate;

    private ZonedDateTime endDate;

    private String webinarMetaData;
    private String webinarSessionId;
    private String webinarSessionName;
    private String webinarSessionDescription;
    private String webinarAggregatedMetaData;
    private String webinarHistorySyncStatus;
    private Integer marketingProgramProgressionId;
    private Boolean headStart;
}
