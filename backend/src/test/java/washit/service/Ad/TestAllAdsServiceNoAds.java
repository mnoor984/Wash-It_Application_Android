package washit.service.Ad;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;

import washit.dao.AdRepository;
import washit.dto.AdDto;
import washit.entity.Ad;
import washit.service.AdServiceImpl;
import washit.service.exception.AdException;

@ExtendWith(MockitoExtension.class)
public class TestAllAdsServiceNoAds {

	@Mock
	private AdRepository adRepo;
	
	@InjectMocks
	private AdServiceImpl service;
	
	@BeforeEach
	public void setMockOutput() {		
		lenient().when(adRepo.findAll()).thenAnswer((InvocationOnMock invocation) -> {
			
			ArrayList<Ad> list = new ArrayList<Ad>();
			Iterable<Ad> iterables = new ArrayList<Ad>();
			
			iterables = (Iterable<Ad>) list;
			return iterables;
		});
	}
	
	@Test
	public void testNoAdGetAds() {
		
		List<AdDto> list = null;
		String error = null;
		
		try {
			list = service.findAllAvailableAds();
		}catch(AdException e) {
			error = e.getMessage();
		}
		
		assertNotNull(error);
		assertNull(list);
		assertEquals("No advertisement exists", error);
		
	}
	
}
