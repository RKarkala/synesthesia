package handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MyContactListener implements ContactListener {

	public int numFootContacts;

	public int doorContacts;

	public int blueContact;
	public int greenContact;
	public int orangeContact;
	public int purpleContact;
	public int redContact;
	public int yellowContact;
	public int checkPoint;

	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		if (fa.getUserData() != null) {
			String val = fa.getUserData().toString();
			if(val.equals("foot")) {
				numFootContacts++;
			}else if(val.equals("door")) {
				doorContacts++;
			}else if(val.equals("blueplate")) {
				blueContact++;
			}else if(val.equals("greenplate")) {
				greenContact++;
			}else if(val.equals("orangeplate")) {
				orangeContact++;
			}else if(val.equals("purpleplate")) {
				purpleContact++;
			}else if(val.equals("redplate")) {
				redContact++;
			}else if(val.equals("yellowplate")) {
				yellowContact++;
			}else if(val.equals("checkpoint")) {
				checkPoint++;
			}
		}
		if (fb.getUserData() != null) {
			String val = fb.getUserData().toString();
			if(val.equals("foot")) {
				numFootContacts++;
			}else if(val.equals("door")) {
				doorContacts++;
			}else if(val.equals("blueplate")) {
				blueContact++;
			}else if(val.equals("greenplate")) {
				greenContact++;
			}else if(val.equals("orangeplate")) {
				orangeContact++;
			}else if(val.equals("purpleplate")) {
				purpleContact++;
			}else if(val.equals("redplate")) {
				redContact++;
			}else if(val.equals("yellowplate")) {
				yellowContact++;
			}else if(val.equals("checkpoint")) {
				checkPoint++;
			}
		}

		System.out.println(fa.getUserData() + ", " + fb.getUserData());
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		Fixture fa = contact.getFixtureA();
		Fixture fb = contact.getFixtureB();
		if (fa.getUserData() != null && fa.getUserData().equals("foot")) {
			numFootContacts--;
		}
		if (fb.getUserData() != null && fb.getUserData().equals("foot")) {
			numFootContacts--;
		}
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}

	public boolean isPlayerOnGround() {
		System.out.println(numFootContacts);
		return (numFootContacts > 0);
	}

	public boolean isOver() {
		return (doorContacts > 0);
	}

	public boolean touchedBlue() {
		return (blueContact > 0);
	}

	public boolean touchedGreen() {
		return (greenContact > 0);
	}

	public boolean touchedOrange() {
		return (orangeContact > 0);
	}

	public boolean touchedPurple() {
		return (purpleContact > 0);
	}

	public boolean touchedRed() {
		return (redContact > 0);
	}

	public boolean touchedYellow() {
		return (yellowContact > 0);
	}
	public boolean onCheckpoint() {
		return (checkPoint > 0);
	}

}
