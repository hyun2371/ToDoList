package org.pwc.todo.share.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QShare is a Querydsl query type for Share
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QShare extends EntityPathBase<Share> {

    private static final long serialVersionUID = -858451536L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QShare share = new QShare("share");

    public final org.pwc.todo.common.QTimeBaseEntity _super = new org.pwc.todo.common.QTimeBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final org.pwc.todo.user.domain.QUser owner;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final org.pwc.todo.user.domain.QUser viewer;

    public QShare(String variable) {
        this(Share.class, forVariable(variable), INITS);
    }

    public QShare(Path<? extends Share> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QShare(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QShare(PathMetadata metadata, PathInits inits) {
        this(Share.class, metadata, inits);
    }

    public QShare(Class<? extends Share> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.owner = inits.isInitialized("owner") ? new org.pwc.todo.user.domain.QUser(forProperty("owner")) : null;
        this.viewer = inits.isInitialized("viewer") ? new org.pwc.todo.user.domain.QUser(forProperty("viewer")) : null;
    }

}

