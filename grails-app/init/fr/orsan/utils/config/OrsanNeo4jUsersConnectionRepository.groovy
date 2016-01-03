package fr.orsan.utils.config
import org.neo4j.ogm.session.Session
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.social.connect.*
import org.springframework.social.connect.neo4j.domain.UserConnection
import org.springframework.social.connect.neo4j.repositories.OgmUserConnectionRepository
import org.springframework.social.connect.neo4j.repositories.impl.Neo4jConnectionRepository
import org.springframework.social.connect.neo4j.repositories.impl.OgmUserConnectionRepositoryImpl
/**
 * Created by yawo on 25/11/15.
 */

class OrsanNeo4jUsersConnectionRepository  implements UsersConnectionRepository{

    private ConnectionFactoryLocator connectionFactoryLocator;
    private OgmUserConnectionRepository repository;
    private ConnectionSignUp connectionSignUp;
    private TextEncryptor textEncryptor;

//    public OrsanNeo4jUsersConnectionRepository(Session session, ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor) {
//        this(session,connectionFactoryLocator,textEncryptor,null)
//    }

    public OrsanNeo4jUsersConnectionRepository(Session session, ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor, ConnectionSignUp connectionSignUp) {
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.textEncryptor = textEncryptor;
        this.repository = new OgmUserConnectionRepositoryImpl(session);
        this.connectionSignUp = connectionSignUp;
    }


    public void setConnectionSignUp(ConnectionSignUp connectionSignUp) {
        this.connectionSignUp = connectionSignUp;
    }

    @Override
    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        ConnectionKey key = connection.getKey();

        Collection<UserConnection> dbCons = repository.findByProviderIdAndProviderUserId(key.getProviderId(), key.getProviderUserId());

        if (dbCons.size() == 0 && connectionSignUp != null) {
            String newUserId = connectionSignUp.execute(connection);
            if (newUserId != null)
            {
                createConnectionRepository(newUserId).addConnection(connection);
                return Arrays.asList(newUserId);
            }
        }

        List<String> userIds = new ArrayList<String>();
        for(UserConnection dbCon: dbCons){
            userIds.add(dbCon.userId);
        }
        return userIds;
    }

    @Override
    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {

        final Set<String> localUserIds = new HashSet<String>();
        List<String> providerUserIdsList = new ArrayList<String>();
        providerUserIdsList.addAll(providerUserIds);
        Iterable<UserConnection> dbCons = repository.findByProviderIdAndProviderUserIdIn(providerId, providerUserIdsList);
        for(UserConnection dbCon:dbCons) {
            localUserIds.add(dbCon.userId);
        }
        return localUserIds;
    }

    @Override
    public ConnectionRepository createConnectionRepository(String userId) {

        if (userId == null) {
            throw new IllegalArgumentException("userId cannot be null");
        }

        return new Neo4jConnectionRepository(userId, repository, connectionFactoryLocator, textEncryptor);
    }

}
