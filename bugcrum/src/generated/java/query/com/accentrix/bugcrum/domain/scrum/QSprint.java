package com.accentrix.bugcrum.domain.scrum;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QSprint is a Querydsl query type for Sprint
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSprint extends EntityPathBase<Sprint> {

    private static final long serialVersionUID = -2128194911L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSprint sprint = new QSprint("sprint");

    public final StringPath description = createString("description");

    public final DateTimePath<java.util.Date> endDate = createDateTime("endDate", java.util.Date.class);

    public final StringPath goals = createString("goals");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final com.accentrix.bugcrum.domain.bugzilla.QProduct product;

    public final ListPath<SprintBug, QSprintBug> sprintBugs = this.<SprintBug, QSprintBug>createList("sprintBugs", SprintBug.class, QSprintBug.class, PathInits.DIRECT2);

    public final DateTimePath<java.util.Date> startDate = createDateTime("startDate", java.util.Date.class);

    public QSprint(String variable) {
        this(Sprint.class, forVariable(variable), INITS);
    }

    public QSprint(Path<? extends Sprint> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSprint(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSprint(PathMetadata<?> metadata, PathInits inits) {
        this(Sprint.class, metadata, inits);
    }

    public QSprint(Class<? extends Sprint> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new com.accentrix.bugcrum.domain.bugzilla.QProduct(forProperty("product"), inits.get("product")) : null;
    }

}

