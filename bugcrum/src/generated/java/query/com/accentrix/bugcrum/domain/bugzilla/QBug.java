package com.accentrix.bugcrum.domain.bugzilla;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QBug is a Querydsl query type for Bug
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QBug extends EntityPathBase<Bug> {

    private static final long serialVersionUID = 915483127L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBug bug = new QBug("bug");

    public final QUser assignedTo;

    public final QComponent component;

    public final NumberPath<Double> estimatedTime = createNumber("estimatedTime", Double.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath priority = createString("priority");

    public final QProduct product;

    public final NumberPath<Double> remainingTime = createNumber("remainingTime", Double.class);

    public final QUser reporter;

    public final StringPath resolution = createString("resolution");

    public final StringPath severity = createString("severity");

    public final StringPath shortDesc = createString("shortDesc");

    public final StringPath status = createString("status");

    public QBug(String variable) {
        this(Bug.class, forVariable(variable), INITS);
    }

    public QBug(Path<? extends Bug> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QBug(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QBug(PathMetadata<?> metadata, PathInits inits) {
        this(Bug.class, metadata, inits);
    }

    public QBug(Class<? extends Bug> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.assignedTo = inits.isInitialized("assignedTo") ? new QUser(forProperty("assignedTo")) : null;
        this.component = inits.isInitialized("component") ? new QComponent(forProperty("component"), inits.get("component")) : null;
        this.product = inits.isInitialized("product") ? new QProduct(forProperty("product"), inits.get("product")) : null;
        this.reporter = inits.isInitialized("reporter") ? new QUser(forProperty("reporter")) : null;
    }

}

