package cn.whatisee.mq;

public class Foo {
    private String Id;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    private String Name;

    public void listen(String message) {
        System.out.println(message);
    }
}

    
    
    
    
    
    
    
    
    
    