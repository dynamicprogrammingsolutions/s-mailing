package dps.simplemailing.manage;

import dps.simplemailing.entities.GeneratedMail;
import dps.simplemailing.entities.QueuedMail;
import dps.simplemailing.mailqueue.MailGenerator;
import dps.simplemailing.mailqueue.PreferredGenerator;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.enterprise.util.AnnotationLiteral;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 *
 * @author ferenci84
 */
@ApplicationScoped
public class GeneratedMailManager extends ManagerBase<GeneratedMail,Long> {

    @Inject
    Instance<MailGenerator> mailGenerator;

    /*public GeneratedMail getById(Long id)
    {
        return em.find(GeneratedMail.class,id);
    }*/

    MailGenerator generatorInstance;

    @PostConstruct
    public void init()
    {
        if (mailGenerator.isAmbiguous()) {
            generatorInstance = mailGenerator.select(new AnnotationLiteral<PreferredGenerator>() {}).get();
        } else {
            generatorInstance = mailGenerator.get();
        }
    }
    
    @Transactional(Transactional.TxType.REQUIRED)
    public GeneratedMail generateMail(QueuedMail queuedMail)
    {
        //MailGenerator generatorInstance = null;



        /*
        generatedMail.setFromEmail(queuedMail.getMail().getFrom());
        generatedMail.setToEmail(queuedMail.getUser().getEmail());
        generatedMail.setSubject(queuedMail.getMail().getSubject());
        
        String body_text = queuedMail.getMail().getBody_text();
        body_text = processPlaceholders(queuedMail.getUser(), body_text);
        
        generatedMail.setBody(body_text);*/


        GeneratedMail generatedMail = generatorInstance.generateMail(new GeneratedMail(),queuedMail.getMail(),queuedMail.getUser());

        //crud.create(generatedMail);
        this.create(generatedMail);

        /*
        queuedMail.setGeneratedMail(generatedMail);
        mailQueue.modify(queuedMail);
        */

        return generatedMail;
                
    }

    
}
