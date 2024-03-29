package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.exceptions.GameCreationException;
import com.floorcorn.tickettoride.exceptions.UserCreationException;
import com.floorcorn.tickettoride.model.GameInfo;
import com.floorcorn.tickettoride.model.PlayerColor;
import com.floorcorn.tickettoride.model.PlayerInfo;
import com.floorcorn.tickettoride.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by Tyler on 2/6/17.
 */

public class ServerProxyUnitTest {
	private ServerProxy sp;

	@Before
	public void setup() {
		sp = new ServerProxy();
		sp.setHost("localhost");
		sp.setPort("8080");
	}

	@After
	public void teardown() {

	}

	@Test
	public void testLogin() {
		User goodUser = new User("tyler", "dragonman");
		try {
			User res = sp.register(goodUser);
			assertNotEquals(res, null);
		} catch(Exception e) {
			System.err.println(e.getMessage());
			assertTrue(false);
		}
		goodUser = new User("tyler", "dragonman");
		User res = null;
		try {
			res = sp.login(goodUser);
		} catch(BadUserException e) {
			e.printStackTrace();
			assertFalse(true);
		}
		assertNotEquals(res, null);
		assertEquals(res.getUsername(), "tyler");
		assertEquals(res.getPassword(), "dragonman");
		assertNotEquals(res.getToken(), null);
		assertNotEquals(res.getUserID(), -1);
		assertEquals(res.getFullName(), "tyler");

		//
		//REGISTER TESTS
		//

		//SHORT USERNAME
		User badUser = new User("t", "dragonman");
		try {
			res = sp.register(badUser);
			assertFalse(true);
		} catch(UserCreationException e) {
			assertTrue(true);
		}

		//SHORT PASSWORD
		badUser = new User("tylerd", "short");
		try {
			res = sp.register(badUser);
			assertFalse(true);
		} catch(UserCreationException e) {
			assertTrue(true);
		}

		//REPEAT USER
		badUser = new User("tyler", "dragonman");
		try {
			res = sp.register(badUser);
			assertFalse(true);
		} catch(UserCreationException e) {
			assertTrue(true);
		}

		//NO USER
		badUser = null;
		try {
			res = sp.register(badUser);
			assertEquals(res, null); //TODO since server gets empty body it responds with bad request.
			// ok since user doesn't need to know its null?
		} catch(UserCreationException e) {
			assertFalse(true);
		}

		//
		//LOGIN TESTS
		//

		//BAD PASSWORD
		badUser = new User("tyler", "notthepassword");
		try {
			res = sp.login(badUser);
			assertEquals(res, null);
		} catch(BadUserException e) {
			assertFalse(true);
		}

		//BAD USERNAME
		badUser = new User("tswaggin", "dragonman");
		try {
			res = sp.login(badUser);
			assertEquals(res, null);
		} catch(BadUserException e) {
			assertFalse(true);
		}

		//PASSWORD ONLY
		badUser = new User("", "dragonman");
		try {
			res = sp.login(badUser);
			assertEquals(res, null);
		} catch(BadUserException e) {
			assertFalse(true);
		}

		//USERNAME ONLY
		badUser = new User("tyler", "");
		try {
			res = sp.login(badUser);
			assertEquals(res, null);
		} catch(BadUserException e) {
			assertFalse(true);
		}

		//BAD TOKEN
		badUser = new User("ABCDEFGHIJKLMNOP");
		try {
			res = sp.login(badUser);
			assertEquals(res, null);
		} catch(BadUserException e) {
			assertFalse(true);
		}

		//NO USER
		badUser = null;
		try {
			res = sp.login(badUser);
			assertEquals(res, null);
		} catch(BadUserException e) {
			assertFalse(true);
		}

	}

	@Test
	public void testGameOperations() {
		User login = null;
		try {
			login = sp.login(new User("tyler","dragonman"));
		} catch(BadUserException e) {
			e.printStackTrace();
			assertFalse(true);
		}
		//TODO bad params gives end of file from server.
		GameInfo game = null;
		try {
			game = sp.createGame(login, "Test", 3);
		} catch(BadUserException | GameCreationException e) {
			e.printStackTrace();
			assertFalse(true);
		}
		assertNotEquals(game, null);
		assertNotEquals(game.getGameID(), -1);
		assertEquals(game.getName(), "Test");
		assertEquals(game.getGameSize(), 3);

		//
		//CHECK GAME LIST
		//
		Set<GameInfo> games = null;
		try {
			games = sp.getGames(login);
		} catch(BadUserException e) {
			e.printStackTrace();
			assertFalse(true);
		}
		assertNotEquals(games, null);
		assertEquals(games.size(), 1);
		int gameID = -1;
		for(GameInfo g : games) {
			assertEquals(g.getGameSize(), game.getGameSize());
			assertEquals(g.getName(), game.getName());
			assertEquals(g.getGameID(), game.getGameID());
			gameID = g.getGameID();
		}

		//
		//JOIN GAME
		//
		try {
			game = sp.joinGame(login, gameID, PlayerColor.BLACK);
		} catch(BadUserException | GameActionException e) {
			e.printStackTrace();
			assertFalse(true);
		}
		assertNotEquals(game, null);
		assertEquals(game.getGameID(), gameID);
		assertFalse(game.hasStarted());
		assertFalse(game.isFinished());
		assertEquals(game.getPlayerList().size(), 1);
		for(PlayerInfo p : game.getPlayerList()) {
			assertEquals(p.getGameID(), game.getGameID());
			assertEquals(p.getColor(), PlayerColor.BLACK);
			assertEquals(p.getUserID(), login.getUserID());
			assertEquals(p.getPlayerID(), 0);
			assertTrue(p.isConductor());
			assertEquals(p.getName(), login.getFullName());
		}

		assertTrue(game.isPlayer(login.getUserID()));
		try {
			game.userCanJoin(login);
			assertFalse(true);
		} catch(BadUserException e) {
			e.printStackTrace();
			assertFalse(true);
		} catch(GameActionException e) {
			assertTrue(true);
		}
		assertEquals(game.getPlayer(login).getUserID(), login.getUserID());
		assertEquals(game.getPlayer(login).getColor(), PlayerColor.BLACK);

		//
		//LEAVE/CANCEL GAME
		//
		boolean left = false;
		try {
			left = sp.leaveGame(login, gameID);
		} catch(BadUserException | GameActionException e) {
			e.printStackTrace();
			assertFalse(true);
		}
		assertTrue(left);

		games = null;
		try {
			games = sp.getGames(login);
		} catch(BadUserException e) {
			e.printStackTrace();
			assertFalse(true);
		}
		assertNotEquals(games, null);
		assertEquals(games.size(), 1);
		for(GameInfo g : games) {
			assertEquals(g.getGameID(), game.getGameID());
			assertTrue(g.isFinished());
		}
	}
}
