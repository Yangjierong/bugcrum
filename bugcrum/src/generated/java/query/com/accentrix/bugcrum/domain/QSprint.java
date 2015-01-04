package com.accentrix.bugcrum.domain;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.accentrix.bugcrum.domain.scrum.Sprint;
import com.mysema.query.types.path.*;
import com.mysema.query.types.PathMetadata;

import javax.annotation.Generated;

import com.mysema.query.types.Path;


/**
 * QSprint is a Querydsl query type for Sprint
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSprint extends EntityPathBase<Sprint> {

    private static final long serialVersionUID = 1910599341L;

    public static final QSprint sprint = new QSprint("sprint");

    public final StringPath description = createString("description");

    public final DateTimePath<java.util.Date> endDate = createDateTime("endDate", java.util.Date.class);

    public final StringPath goals = createString("goals");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final DateTimePath<java.util.Date> startDate = createDateTime("startDate", java.util.Date.class);

    public QSprint(String variable) {
        super(Sprint.class, forVariable(variable));
    }

    public QSprint(Path<? extends Sprint> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSprint(PathMetadata<?> metadata) {
        super(Sprint.class, metadata);
    }

}

