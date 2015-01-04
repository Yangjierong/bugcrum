package com.accentrix.bugcrum.domain.scrum;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QSprintBug is a Querydsl query type for SprintBug
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QSprintBug extends EntityPathBase<SprintBug> {

    private static final long serialVersionUID = 1252697107L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSprintBug sprintBug = new QSprintBug("sprintBug");

    public final com.accentrix.bugcrum.domain.bugzilla.QBug bug;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QSprint sprint;

    public QSprintBug(String variable) {
        this(SprintBug.class, forVariable(variable), INITS);
    }

    public QSprintBug(Path<? extends SprintBug> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSprintBug(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QSprintBug(PathMetadata<?> metadata, PathInits inits) {
        this(SprintBug.class, metadata, inits);
    }

    public QSprintBug(Class<? extends SprintBug> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.bug = inits.isInitialized("bug") ? new com.accentrix.bugcrum.domain.bugzilla.QBug(forProperty("bug"), inits.get("bug")) : null;
        this.sprint = inits.isInitialized("sprint") ? new QSprint(forProperty("sprint"), inits.get("sprint")) : null;
    }

}

