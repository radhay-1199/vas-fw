package gl.core.timer;

public class DoubleyLinketList {
  protected Node start;
  protected Node end;
  public int size;
  protected int timeout;
  
  public DoubleyLinketList(int timeout) {
    this.start = null;
    this.end = null;
    this.size = 0;
    this.timeout = timeout;
  }


  
  public boolean isEmpty() { return (this.start == null); }


  
  public int getSize() { return this.size; }


  
  public void insertAtStart(Integer dly, long t, Data d) {
    Node nptr = new Node(dly, t, d, null, null);
    if (this.start == null) {
      this.start = nptr;
      this.end = this.start;
    } else {
      
      this.start.setLinkPrev(nptr);
      nptr.setLinkNext(this.start);
      this.start = nptr;
    } 
    this.size++;
  }

  
  public synchronized Node insertAtEnd(Integer dly, long t, Data d) {
    Node nptr = new Node(dly, t, d, null, null);
    if (this.start == null) {
      this.start = nptr;
      this.end = this.start;
    } else {
      
      nptr.setLinkPrev(this.end);
      this.end.setLinkNext(nptr);
      this.end = nptr;
    } 
    this.size++;
    return nptr;
  }
  
  public void insertAtPos(Integer dly, long t, Data d, int pos) {
    if (pos == 1) {
      insertAtStart(dly, t, d);
      return;
    } 
    Node nptr = new Node(dly, t, d, null, null);
    Node ptr = this.start;
    for (int i = 2; i <= this.size; i++) {
      if (i == pos) {
        Node tmp = ptr.getLinkNext();
        ptr.setLinkNext(nptr);
        nptr.setLinkPrev(ptr);
        nptr.setLinkNext(tmp);
        tmp.setLinkPrev(nptr);
      } 
      ptr = ptr.getLinkNext();
    } 
    this.size++;
  }

  
  public void deleteAtPos(int pos) {
    if (pos == 1) {
      if (this.size == 1) {
        this.start = null;
        this.end = null;
        this.size = 0;
        return;
      } 
      this.start = this.start.getLinkNext();
      this.start.setLinkPrev(null);
      this.size--;
      return;
    } 
    if (pos == this.size) {
      this.end = this.end.getLinkPrev();
      this.end.setLinkNext(null);
      this.size--;
    } 
    Node ptr = this.start.getLinkNext();
    for (int i = 2; i <= this.size; i++) {
      if (i == pos) {
        Node p = ptr.getLinkPrev();
        Node n = ptr.getLinkNext();
        p.setLinkNext(n);
        n.setLinkPrev(p);
        this.size--;
        return;
      } 
      ptr = ptr.getLinkNext();
    } 
  }
  
  public synchronized void deleteNode(Node ptr) {
    if (ptr == null)
      return; 
    Node p = ptr.getLinkPrev();
    Node n = ptr.getLinkNext();
    if (p == null && n == null) {
      this.start = null;
      this.end = null;
      this.size = 0;
      return;
    } 
    if (p == null) {
      
      n.setLinkPrev(null);
      this.start = n;
    } else {
      
      p.setLinkNext(n);
    } 
    if (n == null) {
      
      p.setLinkNext(null);
      this.end = p;
    } else {
      
      n.setLinkPrev(p);
    } 
    this.size--;
  }

  
  public void display() {
    System.out.print("\nDoubly Linked List = ");
    if (this.size == 0) {
      System.out.print("empty\n");
      return;
    } 
    if (this.start.getLinkNext() == null) {
      System.out.println(this.start.getData());
      return;
    } 
    Node ptr = this.start;
    System.out.print(this.start.getData() + " <-> ");
    ptr = this.start.getLinkNext();
    while (ptr.getLinkNext() != null) {
      System.out.print(ptr.getData() + " <-> ");
      ptr = ptr.getLinkNext();
    } 
    System.out.print(ptr.getData() + "\n");
  }
}
