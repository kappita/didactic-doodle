package tingeso.prestabanco.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tingeso.prestabanco.model.LoanTypeModel;
import tingeso.prestabanco.repository.LoanTypeRepository;

import java.util.List;

@Service
public class LoanTypeService {
    @Autowired
    private LoanTypeRepository loanTypeRepository;

    public List<LoanTypeModel> getAllLoanTypes() {
        return loanTypeRepository.findAll();
    }
}
