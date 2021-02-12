package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientRunService;
import es.udc.ws.app.client.service.ClientRunServiceFactory;
import es.udc.ws.app.client.service.dto.ClientInscriptionDto;

import es.udc.ws.app.client.service.dto.ClientRunDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.model.Run.Run;
import es.udc.ws.app.model.Run.exceptions.MaxParticipantsException;
import es.udc.ws.app.model.runservice.exceptions.*;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class RunServiceClient {

    public static void main(String[] args) {

        if (args.length == 0) {
            printUsage();
        }

        ClientRunService clientRunService = ClientRunServiceFactory.getService();

        if ("-register".equalsIgnoreCase(args[0])) {

            // [registerInRun]    RunServiceClient -register <userEmail> <raceId> <creditCardNumber>

            try {

                clientRunService.registerInRun(Long.valueOf(args[2]), args[1], args[3]);

                System.out.println("Successfully registered in race: #" + args[2]);

            } catch (InstanceNotFoundException | ClientMaxParticipantsException | InputValidationException | ClientLateInscriptionException exception) {
                exception.printStackTrace();
            }

        } else if ("-findRegisters".equalsIgnoreCase(args[0])) {

            // [findInscriptions] RunServiceClient -findRegisters <userEmail>

            try {

                List<ClientInscriptionDto> inscriptionDtos = clientRunService.findInscriptions(args[1]);

                System.out.println("\nFound " + inscriptionDtos.size() + " inscription(s) with e-mail: " + args[1]);
                if (inscriptionDtos.size() == 0) { System.out.println("\n"); }

                for (int i = 0; i < inscriptionDtos.size(); i++) {
                    ClientInscriptionDto inscriptionDto = inscriptionDtos.get(i);
                    System.out.println("\nInscription ID: " + inscriptionDto.getInscriptionId() + "\n" +
                            "   - Run ID: " + inscriptionDto.getRunId() + "\n" +
                            "   - Inscription Dorsal: #" + inscriptionDto.getInscriptionDorsal() + "\n" +
                            "   - Inscription E-Mail: " + inscriptionDto.getInscriptionUserEmail() + "\n"
                    );
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }

        } else if ("-addRace".equalsIgnoreCase(args[0])){

            // -addRace <city> <description> <date> <price> <maxParticipants>

            try {

                ClientRunDto clientRunDto = clientRunService.addRun(new ClientRunDto(
                        null, args[1], args[2], args[3], Float.valueOf(args[4]), Integer.valueOf(args[5]), 0
                ));

                System.out.println("Added run #" + clientRunDto.getRunId());

            } catch (InputValidationException exception) {
                exception.printStackTrace();
            }

        } else if ("-findRace".equalsIgnoreCase(args[0])) {

            // -findRace <raceId>

            try {

                ClientRunDto clientRunDto = clientRunService.findRun(Long.valueOf(args[1]));

                System.out.println(clientRunDto.toString());

            } catch (ClientRunCelebrationException | InstanceNotFoundException e) {
                e.printStackTrace();
            }

        } else if ("-deliverNumber".equalsIgnoreCase(args[0])) {

            // -deliverNumber <id|code> <creditCardNumber>

            try {

                ClientInscriptionDto inscriptionDto = clientRunService.takeDorsal(Long.valueOf(args[1]), args[2]);

                System.out.println("Dorsal #" + inscriptionDto.getInscriptionDorsal() +
                        " for inscription #" + args[1] + " taken for run #" + inscriptionDto.getRunId());

            } catch (ClientDorsalAlreadyTakenException e) {
                e.printStackTrace();
            } catch (ClientInscriptionNotFoundException e) {
                e.printStackTrace();
            } catch (ClientIncorrectCreditCardException e) {
                e.printStackTrace();
            } catch (ClientRunCelebrationException e) {
                e.printStackTrace();
            }

        } else if ("-findRaces".equalsIgnoreCase(args[0])) {

            // -findRaces <date> <city>

            try {

                List<ClientRunDto> runDtos = clientRunService.findRuns(args[2], args[1]);


                for (int i = 0; i < runDtos.size(); i++) {
                    ClientRunDto runDto = runDtos.get(i);
                    System.out.println(runDto.toString());
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }

        } else {
            printUsage();
        }

    }

    public static void printUsage() {

        System.err.println("\nUsage:\n" +
                "   [addRun]           RunServiceClient -addRace <city> <description> <date> <price> <maxParticipants>\n" +
                "   [findRun]          RunServiceClient -findRace <raceId>\n" +
                "   [findRuns]         RunServiceClient -findRaces <date> <city>\n" +
                "   [registerInRun]    RunServiceClient -register <userEmail> <raceId> <creditCardNumber>\n" +
                "   [findInscriptions] RunServiceClient -findRegisters <userEmail>\n" +
                "   [takeDorsal]       RunServiceClient -deliverNumber <id|code> <creditCardNumber>\n");

        System.exit(-1);

    }

}
