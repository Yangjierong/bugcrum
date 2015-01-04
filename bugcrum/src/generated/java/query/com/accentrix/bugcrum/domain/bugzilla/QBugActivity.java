package com.accentrix.bugcrum.domain.bugzilla;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QBugActivity is a Querydsl query type for BugActivity
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QBugActivity extends EntityPathBase<BugActivity> {

    private static final long serialVersionUID = -1165649370L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBugActivity bugActivity = new QBugActivity("bugActivity");

    public final StringPath added = createString("added");

    public final QBug bug;

    public final NumberPath<Integer> fieldId = createNumber("fieldId", Integer.class);

    public final DateTimePath<java.util.Date> when = createDateTime("when", java.util.Date.class);

    public QBugActivity(String variable) {
        this(BugActivity.class, forVariable(variable), INITS);
    }

    public QBugActivity(Path<? extends BugActivity> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QBugActivity(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QBugActivity(PathMetadata<?> metadata, PathInits inits) {
        this(BugActivity.class, metadata, inits);
    }

    public QBugActivity(Class<? extends BugActivity> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bug = inits.isInitialized("bug") ? new QBug(forProperty("bug"), inits.get("bug")) : null;
    }

}

