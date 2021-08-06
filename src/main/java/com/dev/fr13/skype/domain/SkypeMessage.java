package com.dev.fr13.skype.domain;

public class SkypeMessage {
    private static final String MESSAGE_TYPE = "message";
    private Conversation conversation;
    private From from;
    private String type;
    private String text;

    public SkypeMessage(String conversationId, String text) {
        this.conversation = new Conversation(conversationId);
        this.from = new From();
        this.type = MESSAGE_TYPE;
        this.text = text;
    }

    public SkypeMessage() {
    }

    public String getType() {
        return type;
    }

    public Conversation getConversation() {
        return conversation;
    }

    public String getConversationId() {
        return conversation.getId();
    }

    public From getFrom() {
        return from;
    }

    public String getText() {
        return text;
    }

    public static class Conversation {
        private String id;

        public Conversation(String id) {
            this.id = id;
        }

        public Conversation() {
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return "Conversation{" +
                    "id='" + id + '\'' +
                    '}';
        }
    }

    public static class From {
        private final String id;

        public From() {
            this.id = "";
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return "From{" +
                    "id='" + id + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SkypeMessage{" +
                "conversation=" + conversation +
                ", from=" + from +
                ", type='" + type + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
