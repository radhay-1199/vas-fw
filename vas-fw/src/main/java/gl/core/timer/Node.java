package gl.core.timer;

class Node {
  protected Integer delay;
  protected long timeout;
  protected Data data;
  protected Node next;
  protected Node prev;
  
  public Node() {
    this.next = null;
    this.prev = null;
    this.timeout = 0L;
    this.delay = null;
    this.data = null;
  }
  
  public Node(Integer dly, long t, Data d, Node n, Node p) {
    this.delay = dly;
    this.timeout = t;
    this.data = d;
    this.next = n;
    this.prev = p;
  }

  
  public void setLinkNext(Node n) { this.next = n; }


  
  public void setLinkPrev(Node p) { this.prev = p; }


  
  public Node getLinkNext() { return this.next; }


  
  public Node getLinkPrev() { return this.prev; }


  
  public void setData(Data d) { this.data = d; }


  
  public Data getData() { return this.data; }

  
  public void setTimeStamp(long t) { this.timeout = t; }

  
  public long getTimeStamp() { return this.timeout; }

  
  public void setDelayTime(Integer dly) { this.delay = dly; }

  
  public Integer getDelayTime() { return this.delay; }
}
