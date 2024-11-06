package tingeso.prestabanco;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import tingeso.prestabanco.dto.MortgageSimulationRequest;
import tingeso.prestabanco.model.ClientModel;
import tingeso.prestabanco.model.DocumentTypeModel;
import tingeso.prestabanco.model.LoanStatusModel;
import tingeso.prestabanco.model.LoanTypeModel;
import tingeso.prestabanco.repository.LoanTypeRepository;
import tingeso.prestabanco.service.MortgageLoanSimulationService;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class MortgageLoanSimulationServiceTest {
    @Mock
    LoanTypeRepository loanTypeRepository;

    @InjectMocks
    private MortgageLoanSimulationService mortgageLoanSimulationService;

    private ClientModel mockClient;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        DocumentTypeModel mockDocumentType_1 = new DocumentTypeModel(1L, "Comprobante de Ingresos", null);
        DocumentTypeModel mockDocumentType_2 = new DocumentTypeModel(2L, "Certificado de Avalúo", null);
        DocumentTypeModel mockDocumentType_3 = new DocumentTypeModel(3L, "Historial crediticio", null);
        LoanTypeModel mockLoanType_1 = new LoanTypeModel(1L, "Primera vivienda", 30, 0.035F, 0.05F, 0.8F, List.of(mockDocumentType_1, mockDocumentType_2, mockDocumentType_3));
        when(loanTypeRepository.findById(1L)).thenReturn(Optional.of(mockLoanType_1));
        mockClient = new ClientModel();
        mockClient.setBirth_date(new Date(102, 1, 1));
    }

    @Test
    public void testSimulateBadIncome() {
        MortgageSimulationRequest req = new MortgageSimulationRequest(1L, 15, 100000L, 0.045F, 10L, 0L, 1000000L);
        Assertions.assertDoesNotThrow(() -> mortgageLoanSimulationService.simulate(req, mockClient));
    }

    @Test
    public void testSimulateGoodIncome() {
        MortgageSimulationRequest req = new MortgageSimulationRequest(1L, 15, 100000L, 0.045F, 1000000L, 0L, 1000000L);
        Assertions.assertDoesNotThrow(() -> mortgageLoanSimulationService.simulate(req, mockClient));
    }

    @Test
    public void testSimulateBadDebt() {
        MortgageSimulationRequest req = new MortgageSimulationRequest(1L, 15, 100000L, 0.045F, 1000000L, 1000000000L, 1000000L);
        Assertions.assertDoesNotThrow(() -> mortgageLoanSimulationService.simulate(req, mockClient));
    }

    @Test
    public void testSimulateGoodDebt() {
        MortgageSimulationRequest req = new MortgageSimulationRequest(1L, 15, 100000L, 0.045F, 1000000L, 0L, 1000000L);
        Assertions.assertDoesNotThrow(() -> mortgageLoanSimulationService.simulate(req, mockClient));
    }

    @Test
    public void testSimulateBadFinance() {
        MortgageSimulationRequest req = new MortgageSimulationRequest(1L, 15, 100000L, 0.045F, 1000000L, 0L, 1000L);
        Assertions.assertDoesNotThrow(() -> mortgageLoanSimulationService.simulate(req, mockClient));
    }

    @Test
    public void testSimulateGoodFinance() {
        MortgageSimulationRequest req = new MortgageSimulationRequest(1L, 15, 100000L, 0.045F, 1000000L, 0L, 1000000L);
        Assertions.assertDoesNotThrow(() -> mortgageLoanSimulationService.simulate(req, mockClient));
    }

    @Test
    public void testSimulateBadAge() {
        MortgageSimulationRequest req = new MortgageSimulationRequest(1L, 15, 100000L, 0.045F, 1000000L, 0L, 1000000L);
        mockClient.setBirth_date(new Date(10, 1, 1));
        Assertions.assertDoesNotThrow(() -> mortgageLoanSimulationService.simulate(req, mockClient));
    }

    @Test
    public void testSimulateGoodAge() {
        MortgageSimulationRequest req = new MortgageSimulationRequest(1L, 15, 100000L, 0.045F, 1000000L, 0L, 1000000L);
        Assertions.assertDoesNotThrow(() -> mortgageLoanSimulationService.simulate(req, mockClient));
    }


}
