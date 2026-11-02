package com.example.demo.domain.services;

import com.example.demo.domain.exceptions.ShopExceptions;
import com.example.demo.domain.model.Client;
import com.example.demo.domain.model.Shop;
import com.example.demo.infrastructure.repository.ClientRespository;
import com.example.demo.infrastructure.repository.ShopRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShopServiceTest {

    @InjectMocks
    private ShopService shopService;

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private ClientRespository clientRepository;

    @Test
    public void getShopById_existante_renvoieLaBoutique() {
        // GIVEN
        Shop shop = new Shop("Orange", "Orange");
        shop.setId(1);
        when(shopRepository.existsById(1)).thenReturn(true);
        when(shopRepository.findById(1)).thenReturn(Optional.of(shop));

        // WHEN
        Optional<Shop> result = shopService.getShopById(1);

        // THEN
        assertEquals(Optional.of(shop), result);
    }

    @Test
    public void getShopById_inconnue_leveShopExceptions() {
        // GIVEN
        when(shopRepository.existsById(99)).thenReturn(false);

        // WHEN / THEN
        assertThrows(ShopExceptions.class, () -> shopService.getShopById(99));
        verify(shopRepository, never()).findById(any());
    }

    @Test
    public void getClientsByShop_existante_renvoieSesClients() {
        // GIVEN
        Client client = new Client();
        when(shopRepository.existsById(1)).thenReturn(true);
        when(clientRepository.findByShopId(1)).thenReturn(List.of(client));

        // WHEN
        List<Client> result = shopService.getClientsByShop(1);

        // THEN
        assertEquals(List.of(client), result);
    }

    @Test
    public void getClientsByShop_inconnue_leveShopExceptions() {
        // GIVEN
        when(shopRepository.existsById(99)).thenReturn(false);

        // WHEN / THEN
        assertThrows(ShopExceptions.class, () -> shopService.getClientsByShop(99));
        verify(clientRepository, never()).findByShopId(any());
    }

    @Test
    public void siShopExisted_existante_renvoieTrue() {
        // GIVEN
        when(shopRepository.existsById(1)).thenReturn(true);

        // WHEN / THEN
        assertTrue(shopService.siShopExisted(1));
    }

    @Test
    public void siShopExisted_inconnue_leveShopExceptions() {
        // GIVEN
        when(shopRepository.existsById(99)).thenReturn(false);

        // WHEN / THEN
        assertThrows(ShopExceptions.class, () -> shopService.siShopExisted(99));
    }
}
