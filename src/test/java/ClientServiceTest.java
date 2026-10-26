import com.example.demo.application.dto.ClientCreateRequest;
import com.example.demo.application.dto.ClientResponse;
import com.example.demo.domain.model.Client;
import com.example.demo.domain.model.Shop;
import com.example.demo.domain.model.User;
import com.example.demo.domain.services.ClientService;
import com.example.demo.domain.services.ShopService;
import com.example.demo.infrastructure.repository.ClientRespository;
import com.example.demo.infrastructure.repository.ShopRepository;
import com.example.demo.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRespository clientRespository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ShopService shopService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void createClient() {
        // GIVEN
        String email = "alexandre@coliback.com";
        String name = "alexandre";
        String password = "ihuhjkjhjkhkhk";
        String phone = "0657545434";
        Integer shop_id = 1;
        Shop shop = new Shop("Orange", "Orange");

        ClientCreateRequest client_demande = new ClientCreateRequest(name, email, password, phone);

        Client new_client = new Client();
        new_client.setName(name);
        new_client.setPhone(phone);
        new_client.setEmail(email);
        new_client.setShop(shop);
        new_client.setSolde_points(0);


        when(shopService.getShopById(shop_id)).thenReturn(Optional.of(shop));
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(clientRespository.save(any(Client.class))).thenReturn(new_client);
        // WHEN
        ClientResponse resultat = clientService.createClient(client_demande, shop_id);

        // THEN
        verify(clientRespository).save(any(Client.class));
    };
}
