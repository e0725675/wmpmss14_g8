package at.tuwien.sentimentanalyzer.sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
	 * Simple Hashmap using 2 keys. one itemkey, one groupkey
	 * only supports add and get
	 * @author CLF
	 *
	 * @param <K> - key
	 * @param <G> - groupkey
	 * @param <V> - value
	 */
	public class SimpleGroupMap<G extends Comparable<?>,K extends Comparable<?>,V> {
		HashMap<G, HashMap<K,V>> values = new HashMap<G, HashMap<K,V>>();
		
		public boolean put(K key, V value) {
			if (key == null) {
				return false;
			}
			Collection<HashMap<K,V>> groups = this.values.values();
			for (HashMap<K,V> group : groups) {
				group.put(key, value);
			}
			return true;
		}
		public boolean put(G groupKey, K key, V value) {
			if (groupKey == null || key == null) {
				return false;
			}
			HashMap<K,V> nested = null;
			nested = values.get(groupKey);
			if (nested == null) {
				nested = new HashMap<K,V>();
			}
			nested.put(key, value);
			values.put(groupKey, nested);
			return true;
		}
		public boolean containsKey(K key) {
			Collection<HashMap<K,V>> groups = values.values();
			for (HashMap<K,V> group : groups) {
				if (group.containsKey(key)) {
					return true;
				}
			}
			return false;
		}
		public boolean containsGroup(G groupKey) {
			return values.containsKey(groupKey);
		}
		public boolean contains(G groupKey, K key) {
			HashMap<K,V> nested = values.get(groupKey);
			if (nested == null) {
				return false;
			} else {
				return nested.containsKey(key);
			}
		}
		public V get(G groupKey, K key) {
			HashMap<K,V> nested = values.get(groupKey);
			if (nested == null) {
				return null;
			} else {
				return nested.get(key);
			}
		}
		public Collection<V> getByKey(K key) {
			ArrayList<V> out = new ArrayList<V>();
			Collection<HashMap<K,V>> groups = this.values.values();
			for (HashMap<K,V> group : groups) {
				V value = group.get(key);
				if (value != null) {
					out.add(value);
				}
			}
			return out;
		}
		public Map<G,V> getByKeyMapped(K key) {
			HashMap<G,V> out = new HashMap<G,V>();
			Set<G> groupKeys = this.values.keySet();
			for (G groupKey : groupKeys) {
				HashMap<K,V> group = this.values.get(groupKey);
				V value = group.get(key);
				if (value != null) {
					out.put(groupKey, value);
				}
			}
			return out;
		}
		public Collection<V> getByGroupKey(G groupKey) {
			HashMap<K,V> nested = values.get(groupKey);
			return nested.values();
		}
		public HashMap<K,V> getByGroupKeyMapped(G groupKey) {
			HashMap<K,V> nested = values.get(groupKey);
			if (nested == null) {
				return new HashMap<K,V>();
			}
			return nested;
		}
		public Set<K> keySet() {
			HashSet<K> out = new HashSet<K>();
			Collection<HashMap<K,V>> groups = this.values.values();
			for (HashMap<K,V> group : groups) {
				out.addAll(group.keySet());
			}
			return out;
		}
		public Set<G> groupKeySet() {
			return values.keySet();
		}
		public Map<G,Set<K>> keyMap() {
			HashMap<G,Set<K>> out = new HashMap<G,Set<K>>();
			Set<G> groupKeys = this.values.keySet();
			for (G groupKey : groupKeys) {
				HashMap<K,V> group = this.values.get(groupKey);
				Set<K> keys = group.keySet();
				out.put(groupKey, keys);
			}
			return out;
		}
		public Collection<V> values() {
			ArrayList<V> out = new ArrayList<V>();
			Collection<HashMap<K,V>> groups = this.values.values();
			for (HashMap<K,V> group : groups) {
				Collection<V> value = group.values();
				out.addAll(value);
			}
			return out;
		}
		public Map<G,Collection<V>> valuesMappedByGroup() {
			HashMap<G,Collection<V>> out = new HashMap<G,Collection<V>>();
			Set<G> groupKeys = this.values.keySet();
			for (G groupKey : groupKeys) {
				HashMap<K,V> group = this.values.get(groupKey);
				out.put(groupKey, group.values());
			}
			return out;
		}
		public Map<K,Map<G,V>> valuesMappedByKey() {
			Map<K,Map<G,V>> out = new HashMap<K,Map<G,V>>();
			Set<K> keys = this.keySet();
			Set<G> groupKeys = this.values.keySet();
			for (G groupKey : groupKeys) {
				HashMap<K,V> group = this.values.get(groupKey);
				for (K key : keys) {
					V value = group.get(key);
					if (value != null) {
						Map<G,V> groupValue = out.get(key);
						if (groupValue == null) {
							groupValue = new HashMap<G,V>();
						}
						
						groupValue.put(groupKey, value);
						out.put(key, groupValue);
					}
				}
			}
			return out;
		}
		@Override
		public String toString() {
			return "SimpleGroupMap [values=" + values + "]";
		}
	}