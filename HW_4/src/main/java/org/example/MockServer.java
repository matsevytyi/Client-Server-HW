package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MockServer {

    public static void processRequest(Message request, DBConnection connection) {

        //switchCase, cType: 1 create, 2 read, 3 delete, 4 update, 5 filter
        //switchcase, create: message = "name price", read = "name", delete = "name", update = "name newName newPrice", filter = "minPrice maxPrice"

        String params = "";

        switch (request.getcType()) {
            case 1:
                params = request.getMessage();
                DAO.createItem(params.split(" ")[0], Integer.parseInt(params.split(" ")[1]), connection);
                break;
            case 2:
                params = request.getMessage();
                DAO.readItem(params, connection);
                break;
            case 3:
                params = request.getMessage();
                DAO.deleteItem(params, connection);
                break;
            case 4:
                params = request.getMessage();
                DAO.updateItem(params.split(" ")[0], params.split(" ")[1], Integer.parseInt(params.split(" ")[2]), connection);
                break;
            case 5:
                params = request.getMessage();
                DAO.filterByPrice(Integer.parseInt(params.split(" ")[0]), Integer.parseInt(params.split(" ")[1]), connection);
                break;
            default:
                System.out.println("received broken Message");
                break;
        }
    }

    public static Message generateMockRequest() {
        //switchCase, cType: 1 create, 2 read, 3 delete, 4 update, 5 filter
        //switchcase, create: message = "name price", read = "name", delete = "name", update = "name newName newPrice", filter = "minPrice maxPrice"

        int randomCType = (int) (Math.random() * 10) % 5;
        int randValue = (int) (Math.random() * 100);

        switch (randomCType) {
            case 0:
                return new Message(1, 0, "test" + randValue + " " + randValue * 100); //create
            case 1:
                return new Message(2, 0, "test" + randValue); //read
            case 2:
                return new Message(3, 0, "test" + randValue); //delete
            case 3:
                return new Message(4, 0, "test" + randValue + " newName" + randValue*2 + " " + (randValue * 100)); //update
            case 4:
                return new Message(5, 0, "" + randValue + " " + randValue*2); //filter
            default:
                return null;
        }

    }


    public static void lauchService(){
        DBConnection connection = new DBConnection();
        ArrayList<Message> mockRequests = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Message request = generateMockRequest();
            System.out.println("Generated " +request);
            mockRequests.add(request);
        }

        mockRequests.add(new Message(3, 0, "test0"));
        mockRequests.add(new Message(1, 0, "test0 100"));
        mockRequests.add(new Message(2, 0, "test0"));
        mockRequests.add(new Message(5, 0, "0 200"));

        for (Message request : mockRequests) {
            processRequest(request, connection);
        }

        connection.shutdown();
    }

    public static void main(String[] args) {
        lauchService();
    }
}
