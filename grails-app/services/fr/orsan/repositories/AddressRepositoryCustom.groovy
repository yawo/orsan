package fr.orsan.repositories
import fr.orsan.domain.Address
/**
 * Created by yawo on 22/11/15.
 */
interface AddressRepositoryCustom { Set<Address> withinRadiumKm(double lat, double lon, double radiusKm) }
