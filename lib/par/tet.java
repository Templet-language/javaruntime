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

package par;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.Queue;

public class tet {
	class Engine {
		private int active;
		private Lock mtx;
		private Condition cv;
		private Queue<Message> ready;
	}

	abstract class Actor {
		private Lock mtx;
		public abstract void recv (Message message,Actor actor);
	}

	class Message {
		private Actor actor;
		private boolean sending;
	}

	public void send(Engine engine,Message message, Actor actor)
	{
		if (message.sending)
		{
			return;
		}
		message.sending = true;
		message.actor =actor;
		//start of the critical section
		engine.mtx.tryLock();
		engine.ready.add(message);
		engine.mtx.unlock();
		//end of the critical section
		engine.cv.notify();
	}

	public boolean access(Message message, Actor actor)
	{
		return message.actor == actor && !message.sending;
	}

	public void tfunc (Engine engine)
	{
		Message message;
		Actor actor;

		try {
			for (;;) {
				//start of the critical section
				engine.mtx.tryLock();
				while (engine.ready.isEmpty()) {
					engine.active--;
					if (!(engine.active == 0)) {
						engine.cv.notify();
						return;
					}
					engine.mtx.wait();
					engine.active++;
					engine.mtx.unlock();
					//end of the critical section
				}
				message = engine.ready.poll(); //Retrieves and removes the head of this queue, or returns null if this queue is empty.

				actor = message.actor;
				//start of the critical section
				actor.mtx.tryLock();
				message.sending = false;
				actor.recv(message, actor);
				actor.mtx.unlock();
				//end of the critical section
			}
		}
		catch (InterruptedException interExc)
		{
			interExc.getMessage();
		}
	}
}