package p12.exercise;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;

public class MultiQueueImpl<T, Q> implements MultiQueue<T, Q>{

    private final Map<Q, LinkedList<T>> map = new HashMap<>();

    public Set<Q> availableQueues() {
        return this.map.keySet();  
    }

    public void openNewQueue(Q queue) {
        if(isQueueAvailable(queue)){
            throw new IllegalArgumentException();
        }

        this.map.put(queue, new LinkedList<T>());
    }

    public boolean isQueueEmpty(Q queue) {
        
        if(!isQueueAvailable(queue)){
            throw new IllegalArgumentException();
        }

        return this.map.get(queue).isEmpty();
    }

    public void enqueue(T elem, Q queue) {
        if(!isQueueAvailable(queue)){
            throw new IllegalArgumentException();
        }
        
        map.get(queue).add(elem);
    }

    public T dequeue(Q queue) {
        if(!isQueueAvailable(queue)){
            throw new IllegalArgumentException();
        }

        return this.map.get(queue).pollFirst();
    }

    @Override
    public Map<Q, T> dequeueOneFromAllQueues() {
        final Map<Q, T> m = new HashMap<>();
        for(Q q: this.map.keySet()){
            m.put(q, this.map.get(q).pollFirst());
        }

        return m;
    }

    @Override
    public Set<T> allEnqueuedElements() {
        final Set<T> s = new HashSet<>();
        for(Q q: this.map.keySet()){
            s.addAll(this.map.get(q));
        }
        return s;
    }

    @Override
    public List<T> dequeueAllFromQueue(Q queue) {
        if(!isQueueAvailable(queue)){
            throw new IllegalArgumentException();
        }

        final List<T> l = new LinkedList<>();
        l.addAll(this.map.get(queue));
        map.get(queue).clear();

        return l;
    }

    @Override
    public void closeQueueAndReallocate(Q queue) {
        boolean thereIsAlternative = false;

        if(!isQueueAvailable(queue)){
            throw new IllegalArgumentException();
        }

        for(final var myIterator = this.map.keySet().iterator(); myIterator.hasNext();){
            if(myIterator.next() != queue){
                thereIsAlternative = true;
                thereIsAlternative(queue, myIterator.next());
            }
        }

        if(!thereIsAlternative){
            throw new IllegalStateException();
        }
    }

    private boolean isQueueAvailable(Q queue){
        return map.containsKey(queue);
    }

    private void thereIsAlternative(Q q, Q alternative){
        final List<T> l = new LinkedList<>();
        l.addAll(dequeueAllFromQueue(q));
        this.map.get(alternative).addAll(l);
        this.map.remove(q);
    }
}
