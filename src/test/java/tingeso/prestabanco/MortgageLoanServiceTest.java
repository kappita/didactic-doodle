package tingeso.prestabanco;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import tingeso.prestabanco.dto.*;
import tingeso.prestabanco.model.*;
import tingeso.prestabanco.repository.*;
import tingeso.prestabanco.service.ExecutiveService;
import tingeso.prestabanco.service.MortgageLoanService;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class MortgageLoanServiceTest {
    @Mock
    private ExecutiveRepository executiveRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MortgageLoanRepository mortgageLoanRepository;

    @Mock
    private LoanTypeRepository loanTypeRepository;

    @Mock
    private LoanStatusRepository loanStatusRepository;

    @Mock
    private MortgageLoanReviewRepository mortgageLoanReviewRepository;

    @Mock
    private PreApprovedMortgageLoanRepository preApprovedMortgageLoanRepository;

    @InjectMocks
    private MortgageLoanService mortgageLoanService;

    private RoleModel mockClientRole;
    private RoleModel mockExecutiveRole;

    private UserModel mockUser_1;
    private UserModel mockUser_2;
    private UserModel mockUser_3;
    private UserModel mockUser_4;

    private ExecutiveModel mockExecutive_1;
    private ExecutiveModel mockExecutive_2;

    private ClientModel mockClient_1;
    private ClientModel mockClient_2;

    private DocumentTypeModel mockDocumentType_1;
    private DocumentTypeModel mockDocumentType_2;
    private DocumentTypeModel mockDocumentType_3;

    private LoanTypeModel mockLoanType_1;

    private LoanStatusModel mockLoanStatus_1;
    private LoanStatusModel mockLoanStatus_2;
    private LoanStatusModel mockLoanStatus_3;
    private LoanStatusModel mockLoanStatus_4;
    private LoanStatusModel mockLoanStatus_5;
    private LoanStatusModel mockLoanStatus_6;
    private LoanStatusModel mockLoanStatus_7;
    private LoanStatusModel mockLoanStatus_8;
    private LoanStatusModel mockLoanStatus_9;


    private MortgageLoanModel mockMortgageLoanModel;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockClientRole = new RoleModel(1L, "CLIENT");
        mockExecutiveRole = new RoleModel(2L, "EXECUTIVE");
        mockUser_1 = new UserModel(1L, "usuario1@testing.com", "ClaveCorrecta123.", mockClientRole);
        mockUser_2 = new UserModel(2L, "usuario2@testing.com", "ClaveCorrecta123.", mockClientRole);
        mockUser_3 = new UserModel(3L, "usuario3@testing.com", "ClaveCorrecta123.", mockExecutiveRole);
        mockUser_4 = new UserModel(4L, "usuario4@testing.com", "ClaveCorrecta123.", mockExecutiveRole);

        mockClient_1 = new ClientModel(mockUser_1, "Ignacio", "Lara", new Date(102, 1, 4), "otro", "Chilena", "Diinf usach", "1234567");
        mockClient_2 = new ClientModel(mockUser_2, "Alcides", "Quispe", new Date(102, 1, 4), "otro", "Chilena", "Diinf usach", "1234567");
        mockExecutive_1 = new ExecutiveModel(mockUser_3, "Ejecutivo 1");
        mockExecutive_2 = new ExecutiveModel(mockUser_4, "Ejecutivo 2");

        mockDocumentType_1 = new DocumentTypeModel(1L, "Comprobante de Ingresos", null);
        mockDocumentType_2 = new DocumentTypeModel(2L, "Certificado de Avalúo", null);
        mockDocumentType_3 = new DocumentTypeModel(3L, "Historial crediticio", null);

        mockLoanStatus_1 = new LoanStatusModel("E1", "En Revisión Inicial", "La solicitud ha sido recibida y está en proceso de verificación preliminar.");
        mockLoanStatus_2 = new LoanStatusModel("E2", "Pendiente de Documentación", "La solicitud está en espera porque falta uno o más documentos importantes o se requiere información adicional del cliente.");
        mockLoanStatus_3 = new LoanStatusModel("E3", "En Evaluación", "La solicitud ha pasado la revisión inicial y está siendo evaluada por un ejecutivo.");
        mockLoanStatus_4 = new LoanStatusModel("E4", "Pre-Aprobada", "La solicitud ha sido evaluada y cumple con los criterios básicos del banco, por lo que ha sido pre-aprobada");
        mockLoanStatus_5 = new LoanStatusModel("E5", "En Aprobación Final", "El cliente ha aceptado las condiciones propuestas, y la solicitud se encuentra en proceso de aprobación final.");
        mockLoanStatus_6 = new LoanStatusModel("E6", "Aprobada", "La solicitud ha sido aprobada y está lista para el desembolso.");
        mockLoanStatus_7 = new LoanStatusModel("E7", "Rechazada", "La solicitud ha sido evaluada y, tras el análisis, no cumple con los criterios establecidos por el banco.");
        mockLoanStatus_8 = new LoanStatusModel("E8", "Cancelada por el Cliente", "El cliente ha decidido cancelar la solicitud antes de que esta sea aprobada.");
        mockLoanStatus_9 = new LoanStatusModel("E9", "En Desembolso", "La solicitud ha sido aprobada y se está ejecutando el proceso de desembolso del monto aprobado");

        when(loanStatusRepository.findById("E1")).thenReturn(Optional.of(mockLoanStatus_1));
        when(loanStatusRepository.findById("E2")).thenReturn(Optional.of(mockLoanStatus_2));
        when(loanStatusRepository.findById("E3")).thenReturn(Optional.of(mockLoanStatus_3));
        when(loanStatusRepository.findById("E4")).thenReturn(Optional.of(mockLoanStatus_4));
        when(loanStatusRepository.findById("E5")).thenReturn(Optional.of(mockLoanStatus_5));
        when(loanStatusRepository.findById("E6")).thenReturn(Optional.of(mockLoanStatus_6));
        when(loanStatusRepository.findById("E7")).thenReturn(Optional.of(mockLoanStatus_7));
        when(loanStatusRepository.findById("E8")).thenReturn(Optional.of(mockLoanStatus_8));
        when(loanStatusRepository.findById("E9")).thenReturn(Optional.of(mockLoanStatus_9));

        mockLoanType_1 = new LoanTypeModel(1L, "Primera vivienda", 30, 0.035F, 0.05F, 0.8F, List.of(mockDocumentType_1, mockDocumentType_2, mockDocumentType_3));

        mockMortgageLoanModel = new MortgageLoanModel(1L, mockClient_1, mockLoanType_1, 15, 100000L, 0.04F, new ArrayList<>(), mockLoanStatus_1, new ArrayList<>());
    }

    @Test
    public void testGetMortgageLoanCorrectOwner() {
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        MortgageLoanModel res = mortgageLoanService.getMortgageLoan(1L, mockUser_1);
        Assertions.assertEquals(mockMortgageLoanModel, res);
    }

    @Test
    public void testGetMortgageLoanWrongOwner() {
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertThrows(ResponseStatusException.class, () -> mortgageLoanService.getMortgageLoan(1L, mockUser_2));
    }

    @Test
    public void testGetMortgageLoanNotExisting() {
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResponseStatusException.class, () -> mortgageLoanService.getMortgageLoan(1L, mockUser_1));
    }

    @Test
    public void testGetAllReviewable() {
        when(mortgageLoanRepository.findAll()).thenReturn(new ArrayList<>());
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.getAllReviewable());
    }

    @Test
    public void testReceiveMortgageInvalidType() {
        MortgageLoanRequest req = new MortgageLoanRequest(69L, 15, 800000L, 0.05F);
        when(loanTypeRepository.findById(69L)).thenReturn(Optional.empty());
        MortgageLoanModel testMortgage = new MortgageLoanModel(req, mockClient_1, mockLoanStatus_1, mockLoanType_1);
        when(mortgageLoanRepository.save(testMortgage)).thenReturn(testMortgage);
        Assertions.assertThrows(ResponseStatusException.class, () -> mortgageLoanService.receiveMortgage(req, mockClient_1));
    }

    @Test
    public void testReceiveMortgageNoStatus() {
        MortgageLoanRequest req = new MortgageLoanRequest(69L, 15, 800000L, 0.05F);
        when(loanTypeRepository.findById(69L)).thenReturn(Optional.empty());
        when(loanStatusRepository.findById("E1")).thenReturn(Optional.empty());
        MortgageLoanModel testMortgage = new MortgageLoanModel(req, mockClient_1, mockLoanStatus_1, mockLoanType_1);
        when(mortgageLoanRepository.save(testMortgage)).thenReturn(testMortgage);
        Assertions.assertThrows(ResponseStatusException.class, () -> mortgageLoanService.receiveMortgage(req, mockClient_1));
    }

    @Test
    public void testReceiveMortgageValidType() {
        MortgageLoanRequest req = new MortgageLoanRequest(1L, 15, 800000L, 0.05F);
        when(loanTypeRepository.findById(1L)).thenReturn(Optional.of(mockLoanType_1));
        when(loanStatusRepository.findById("E1")).thenReturn(Optional.of(mockLoanStatus_1));
        MortgageLoanModel testMortgage = new MortgageLoanModel(req, mockClient_1, mockLoanStatus_1, mockLoanType_1);
        when(mortgageLoanRepository.save(testMortgage)).thenReturn(testMortgage);
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.receiveMortgage(req, mockClient_1));
    }

    @Test
    public void testSetPendingDocumentationWrongStatus() {
        PendingDocumentationRequest req = new PendingDocumentationRequest(List.of(1L), "");
        mockMortgageLoanModel.setStatus(mockLoanStatus_2);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertThrows(ResponseStatusException.class, () -> {mortgageLoanService.setPendingDocumentation(1L, req, mockExecutive_1);});

    }

    @Test
    public void testSetPendingDocumentationNoDocs() {
        PendingDocumentationRequest req = new PendingDocumentationRequest(new ArrayList<>(), "");
        when(loanStatusRepository.findById("E2")).thenReturn(Optional.of(mockLoanStatus_2));
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertThrows(ResponseStatusException.class, () -> {mortgageLoanService.setPendingDocumentation(1L, req, mockExecutive_1);});

    }

    @Test
    public void testSetPendingDocumentation() {
        PendingDocumentationRequest req = new PendingDocumentationRequest(List.of(1L), "");

        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        when(loanStatusRepository.findById("E2")).thenReturn(Optional.of(mockLoanStatus_2));
        MortgageLoanModel step_2 = mockMortgageLoanModel.toBuilder().build();
        step_2.setStatus(mockLoanStatus_2);
        when(mortgageLoanRepository.save(step_2)).thenReturn(step_2);
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.setPendingDocumentation(1L, req, mockExecutive_1));

    }
    @Test
    public void testSetInEvaluationWrongStatus() {
        mockMortgageLoanModel.setStatus(mockLoanStatus_2);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertThrows(ResponseStatusException.class, () -> {mortgageLoanService.setInEvaluation(1L, mockExecutive_1);});
    }

    @Test
    public void testSetInEvaluation() {
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        when(loanStatusRepository.findById("E3")).thenReturn(Optional.of(mockLoanStatus_3));
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.setInEvaluation(1L, mockExecutive_1));
    }

    @Test
    public void testEvaluateMortgageWrongStatus() {
        CreditValidation cValidation = new CreditValidation(99999999L, true, true, 0L, 1000000L);
        SavingCapacity sCapacity = new SavingCapacity(100000000L, true, true, 5, true);
        CreditEvaluation req = new CreditEvaluation(cValidation, sCapacity);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertThrows(ResponseStatusException.class, () -> {mortgageLoanService.evaluateMortgage(1L, req, mockExecutive_1);});
    }

    @Test
    public void testEvaluateMortgageWrongIncome() {
        CreditValidation cValidation = new CreditValidation(1L, true, true, 0L, 1000000L);
        SavingCapacity sCapacity = new SavingCapacity(100000000L, true, true, 5, true);
        CreditEvaluation req = new CreditEvaluation(cValidation, sCapacity);
        mockMortgageLoanModel.setStatus(mockLoanStatus_3);
        when(loanStatusRepository.findById("E7")).thenReturn(Optional.of(mockLoanStatus_7));
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.evaluateMortgage(1L, req, mockExecutive_1));
    }

    @Test
    public void testEvaluateMortgageWrongHistory() {
        CreditValidation cValidation = new CreditValidation(99999999L, false, true, 0L, 1000000L);
        SavingCapacity sCapacity = new SavingCapacity(100000000L, true, true, 5, true);
        CreditEvaluation req = new CreditEvaluation(cValidation, sCapacity);
        mockMortgageLoanModel.setStatus(mockLoanStatus_3);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        when(loanStatusRepository.findById("E7")).thenReturn(Optional.of(mockLoanStatus_7));
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.evaluateMortgage(1L, req, mockExecutive_1));
    }

    @Test
    public void testEvaluateMortgageWrongStability() {
        CreditValidation cValidation = new CreditValidation(99999999L, true, false, 0L, 1000000L);
        SavingCapacity sCapacity = new SavingCapacity(100000000L, true, true, 5, true);
        CreditEvaluation req = new CreditEvaluation(cValidation, sCapacity);
        mockMortgageLoanModel.setStatus(mockLoanStatus_3);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        when(loanStatusRepository.findById("E7")).thenReturn(Optional.of(mockLoanStatus_7));
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.evaluateMortgage(1L, req, mockExecutive_1));
    }

    @Test
    public void testEvaluateMortgageWrongIncomeRelation() {
        CreditValidation cValidation = new CreditValidation(99999999L, true, true, 9999999999L, 1000000L);
        SavingCapacity sCapacity = new SavingCapacity(100000000L, true, true, 5, true);
        CreditEvaluation req = new CreditEvaluation(cValidation, sCapacity);
        mockMortgageLoanModel.setStatus(mockLoanStatus_3);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        when(loanStatusRepository.findById("E7")).thenReturn(Optional.of(mockLoanStatus_7));
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.evaluateMortgage(1L, req, mockExecutive_1));
    }

    @Test
    public void testEvaluateMortgageWrongFinance() {
        CreditValidation cValidation = new CreditValidation(999999L, true, true, 0L, 100L);
        SavingCapacity sCapacity = new SavingCapacity(0L, true, true, 5, true);
        CreditEvaluation req = new CreditEvaluation(cValidation, sCapacity);
        mockMortgageLoanModel.setStatus(mockLoanStatus_3);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        when(loanStatusRepository.findById("E7")).thenReturn(Optional.of(mockLoanStatus_7));
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.evaluateMortgage(1L, req, mockExecutive_1));
    }

    // TODO
    @Test
    public void testEvaluateMortgageWrongMaxAge() {
        mockClient_1.setBirth_date(new Date(10, 1, 1));
        mockMortgageLoanModel.setClient(mockClient_1);
        CreditValidation cValidation = new CreditValidation(99999999L, true, true, 0L, 1000000L);
        SavingCapacity sCapacity = new SavingCapacity(100000000L, true, true, 5, true);
        CreditEvaluation req = new CreditEvaluation(cValidation, sCapacity);
        mockMortgageLoanModel.setStatus(mockLoanStatus_3);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.evaluateMortgage(1L, req, mockExecutive_1));
    }


    @Test
    public void testEvaluateMortgageLongevity1() {
        CreditValidation cValidation = new CreditValidation(99999999L, true, true, 0L, 1000000L);
        SavingCapacity sCapacity = new SavingCapacity(100000000L, true, true, 1, true);
        CreditEvaluation req = new CreditEvaluation(cValidation, sCapacity);
        mockMortgageLoanModel.setStatus(mockLoanStatus_3);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        when(mortgageLoanReviewRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertDoesNotThrow(() -> mortgageLoanService.evaluateMortgage(1L, req, mockExecutive_1));
    }

    @Test
    public void testEvaluateMortgageLongevity2() {
        CreditValidation cValidation = new CreditValidation(99999999L, true, true, 0L, 1000000L);
        SavingCapacity sCapacity = new SavingCapacity(100000000L, true, true, 10, true);
        CreditEvaluation req = new CreditEvaluation(cValidation, sCapacity);
        mockMortgageLoanModel.setStatus(mockLoanStatus_3);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.evaluateMortgage(1L, req, mockExecutive_1));
    }

    @Test
    public void testEvaluateCorrect() {
        CreditValidation cValidation = new CreditValidation(99999999L, true, true, 0L, 1000000L);
        SavingCapacity sCapacity = new SavingCapacity(100000000L, true, true, 10, true);
        CreditEvaluation req = new CreditEvaluation(cValidation, sCapacity);
        mockMortgageLoanModel.setStatus(mockLoanStatus_3);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.evaluateMortgage(1L, req, mockExecutive_1));
    }

    @Test
    public void testEvaluateRequiresReview() {
        CreditValidation cValidation = new CreditValidation(99999999L, true, true, 0L, 1000000L);
        SavingCapacity sCapacity = new SavingCapacity(100000000L, false, true, 10, true);
        CreditEvaluation req = new CreditEvaluation(cValidation, sCapacity);
        mockMortgageLoanModel.setStatus(mockLoanStatus_3);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.evaluateMortgage(1L, req, mockExecutive_1));
    }

    @Test
    public void testEvaluateRejected() {
        CreditValidation cValidation = new CreditValidation(99999999L, true, true, 0L, 1000000L);
        SavingCapacity sCapacity = new SavingCapacity(0L, false, false, 10, false);
        CreditEvaluation req = new CreditEvaluation(cValidation, sCapacity);
        mockMortgageLoanModel.setStatus(mockLoanStatus_3);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.evaluateMortgage(1L, req, mockExecutive_1));
    }

    @Test
    public void testSetFinalApprovalWrongStatus() {
        mockMortgageLoanModel.setStatus(mockLoanStatus_3);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertThrows(ResponseStatusException.class, () -> mortgageLoanService.setFinalApproval(1L, mockClient_1));
    }

    @Test
    public void testSetFinalApprovalCorrect() {
        mockMortgageLoanModel.setStatus(mockLoanStatus_4);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.setFinalApproval(1L, mockClient_1));
    }

    @Test
    public void testSetApprovedWrongStatus() {
        mockMortgageLoanModel.setStatus(mockLoanStatus_4);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertThrows(ResponseStatusException.class, () -> mortgageLoanService.setApproved(1L, mockExecutive_1));
    }



    @Test
    public void testSetApprovedCorrect() {
        mockMortgageLoanModel.setStatus(mockLoanStatus_5);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.setApproved(1L, mockExecutive_1));
    }

    @Test
    public void testSetRejectedWrongStatus() {
        mockMortgageLoanModel.setStatus(mockLoanStatus_6);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertThrows(ResponseStatusException.class, () -> mortgageLoanService.setRejected(1L, mockExecutive_1));
    }

    @Test
    public void cancelMortgageByClientRightClient() {
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.cancelMortgageByClient(1L, mockClient_1));
    }

    @Test
    public void cancelMortgageByClientWrongClient() {
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertThrows(ResponseStatusException.class, () -> mortgageLoanService.cancelMortgageByClient(1L, mockClient_2));
    }

    @Test
    public void testSetInOutgoWrongStatus() {
        mockMortgageLoanModel.setStatus(mockLoanStatus_5);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertThrows(ResponseStatusException.class, () -> mortgageLoanService.setInOutgo(1L, mockExecutive_1));
    }

    @Test
    public void testSetInOutgoCorrect() {
        mockMortgageLoanModel.setStatus(mockLoanStatus_6);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.setInOutgo(1L, mockExecutive_1));
    }

    @Test
    public void testReviewMortgageNotExists() {
        when(mortgageLoanReviewRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(IllegalArgumentException.class, () -> {mortgageLoanService.reviewMortgage(1L, null, mockExecutive_1);});
    }

    @Test
    public void testReviewSameReviewer() {
        MortgageLoanReviewModel review = new MortgageLoanReviewModel(mockMortgageLoanModel, mockExecutive_1);
        when(mortgageLoanReviewRepository.findById(1L)).thenReturn(Optional.of(review));
        Assertions.assertThrows(IllegalArgumentException.class, () -> mortgageLoanService.reviewMortgage(1L, null, mockExecutive_1));
    }

    @Test
    public void testReviewCorrectReviewer() {
        MortgageLoanReviewModel review = new MortgageLoanReviewModel(mockMortgageLoanModel, mockExecutive_1);
        MortgageReview req = new MortgageReview(true);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        when(mortgageLoanReviewRepository.findById(1L)).thenReturn(Optional.of(review));
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.reviewMortgage(1L, req, mockExecutive_2));
    }

    @Test
    public void testReviewReject() {
        MortgageLoanReviewModel review = new MortgageLoanReviewModel(mockMortgageLoanModel, mockExecutive_1);
        MortgageReview req = new MortgageReview(false);
        mockMortgageLoanModel.setStatus(mockLoanStatus_3);
        when(mortgageLoanRepository.findById(1L)).thenReturn(Optional.of(mockMortgageLoanModel));
        when(mortgageLoanReviewRepository.findById(1L)).thenReturn(Optional.of(review));
        Assertions.assertDoesNotThrow(() -> mortgageLoanService.reviewMortgage(1L, req, mockExecutive_2));
    }
}
