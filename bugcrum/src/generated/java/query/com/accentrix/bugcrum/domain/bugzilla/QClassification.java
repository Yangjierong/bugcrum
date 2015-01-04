package com.accentrix.bugcrum.domain.bugzilla;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;


/**
 * QClassification is a Querydsl query type for Classification
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QClassification extends EntityPathBase<Classification> {

    private static final long serialVersionUID = 439961731L;

    public static final QClassification classification = new QClassification("classification");

    public final StringPath description = createString("description");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath name = createString("name");

    public final NumberPath<Integer> sortkey = createNumber("sortkey", Integer.class);

    public QClassification(String variable) {
        super(Classification.class, forVariable(variable));
    }

    public QClassification(Path<? extends Classification> path) {
        super(path.getType(), path.getMetadata());
    }

    public QClassification(PathMetadata<?> metadata) {
        super(Classification.class, metadata);
    }

}

