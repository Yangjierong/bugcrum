package com.accentrix.bugcrum.domain.bugzilla;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QBugStatus is a Querydsl query type for BugStatus
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QBugStatus extends EntityPathBase<BugStatus> {

    private static final long serialVersionUID = -1012641591L;

    public static final QBugStatus bugStatus = new QBugStatus("bugStatus");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final BooleanPath isOpen = createBoolean("isOpen");

    public final NumberPath<Integer> sortkey = createNumber("sortkey", Integer.class);

    public final StringPath value = createString("value");

    public QBugStatus(String variable) {
        super(BugStatus.class, forVariable(variable));
    }

    public QBugStatus(Path<? extends BugStatus> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBugStatus(PathMetadata<?> metadata) {
        super(BugStatus.class, metadata);
    }

}

