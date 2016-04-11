/*--------------------------------------------------------------------------*/
/*  Copyright 2015 Sergey Vostokin, Ekaterina Skoryupina                    */
/*                                                                          */
/*  Licensed under the Apache License, Version 2.0 (the "License");         */
/*  you may not use this file except in compliance with the License.        */
/*  You may obtain a copy of the License at                                 */
/*                                                                          */
/*  http://www.apache.org/licenses/LICENSE-2.0                              */
/*                                                                          */
/*  Unless required by applicable law or agreed to in writing, software     */
/*  distributed under the License is distributed on an "AS IS" BASIS,       */
/*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.*/
/*  See the License for the specific language governing permissions and     */
/*  limitations under the License.                                          */
/*--------------------------------------------------------------------------*/

package seq;
import java.util.Queue;

public class tet {
	class Engine {
		private Queue<Message> ready;
	}

	abstract class Actor {
		public abstract void recv(Message message, Actor actor);
	}

	class Message {
		private Actor actor;
		private boolean sending;
	}

	public void send(Engine engine, Message message, Actor actor) {
		if (message.sending) {
			return;
		}
		message.sending = true;
		message.actor = actor;
		engine.ready.add(message);
	}

	public boolean access(Message message, Actor actor) {
		return message.actor == actor && !message.sending;
	}

	public void run(Engine e) {
		while (e.ready.size() != 0) {
			Message c = e.ready.poll();
			c.sending = false;
			c.actor.recv(c, c.actor);
		}
	}
}