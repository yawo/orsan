package fr.orsan.repositories
import fr.orsan.domain.Address
import org.springframework.data.neo4j.repository.GraphRepository
import org.springframework.stereotype.Repository

@Repository
public interface AddressRepository extends GraphRepository<Address>, AddressRepositoryCustom {}
