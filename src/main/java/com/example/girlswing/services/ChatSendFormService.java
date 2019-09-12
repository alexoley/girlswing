package com.example.girlswing.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import java.awt.*;
import java.util.Optional;
import java.util.Vector;

@Service
@Slf4j
public class ChatSendFormService {

    @Autowired
    ResponseService responseService;

    @Autowired
    RequestService requestService;

    @Autowired
    ConnectionService connectionService;

    @Autowired
    MainFormService mainFormService;

    public void fillFilterComboBoxWithItemFromJsonNode(JsonNode jsonNode, JComboBox jComboBox, String dictionaryId){
        Vector model = new Vector();
        model.add(new Item("","" ));
        if(jsonNode.findPath("data").isArray()){
            for (JsonNode dictionary : jsonNode.findPath("data")) {
                if(dictionaryId.equals(dictionary.findPath("id").toString())){
                    for (JsonNode item : dictionary.findPath("dictionary")){
                        model.addElement( new Item(item.findPath("id").toString(), item.findPath("text").toString() ) );
                    }
                }
            }
        }
        jComboBox.setRenderer(new ItemRenderer());
        jComboBox.setModel(new DefaultComboBoxModel(model));

    }

    public JsonNode getFilterValues(){
        Optional response = responseService.returnResponseBodyJsonNode(requestService.getDictionaries());
        if(response.isPresent()){
            return (JsonNode) response.get();
        }
        return new ObjectNode(new JsonNodeFactory(true));
    }

    class ItemRenderer extends BasicComboBoxRenderer
    {
        public Component getListCellRendererComponent(
                JList list, Object value, int index,
                boolean isSelected, boolean cellHasFocus)
        {
            return super.getListCellRendererComponent(list, value, index,
                    isSelected, cellHasFocus);

            /*if (value != null)
            {
                Item item = (Item)value;
                setText( item.getDescription());
            }

            if (index == -1)
            {
                Item item = (Item)value;
                setText(item.getId());
            }


            return this;*/
        }
    }

    class Item
    {
        private String id;
        private String description;

        public Item(String id, String description)
        {
            this.id = id;
            this.description = description;
        }

        public String getId()
        {
            return id;
        }

        public String getDescription()
        {
            return description;
        }

        public String toString()
        {
            return getDescription().replace("\"","");
        }
    }

    /*public void getAllActiveUsersAndSendThemMessage(String text) {
        new Thread(()->{
            ConnectionBox cb;
            Set<Connection> uniqueConnections = new HashSet<>();
            Set<Connection> oneRun = new HashSet<>();
            for (int i = 0; i < 5; i++) {
                try{
                    cb=connectionService.getConnectionBox(0,i);
                    uniqueConnections.addAll(cb.getConnections());
                    oneRun.addAll(cb.getConnections());}
                catch(ConnectionEmptyException e){
                    break;
                }
                do{
                    try {
                        cb = connectionService.getConnectionBox(0, i, (String) cb.getCursor());
                        uniqueConnections.addAll(cb.getConnections());
                        oneRun.addAll(cb.getConnections());
                    }
                    catch(ConnectionEmptyException e){
                        break;
                    }
                }
                while(true);
                oneRun.removeAll(uniqueConnections);
                mainFormService.sendToAllFromList(oneRun.stream().map(conn -> conn.getIdMale()).collect(Collectors.toList()),
                        text);

            }
            log.debug(uniqueConnections.size()+"");
            uniqueConnections.forEach(conn -> log.debug(conn.toString()));
        }).start();
    }*/

}
