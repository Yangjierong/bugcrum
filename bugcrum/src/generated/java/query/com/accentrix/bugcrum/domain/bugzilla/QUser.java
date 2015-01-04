package com.accentrix.bugcrum.domain.bugzilla;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1684229976L;

    public static final QUser user = new QUser("user");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath isEnabled = createBoolean("isEnabled");

    public final StringPath loginName = createString("loginName");

    public final StringPath realName = createString("realName");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata<?> metadata) {
        super(User.class, metadata);
    }

}

