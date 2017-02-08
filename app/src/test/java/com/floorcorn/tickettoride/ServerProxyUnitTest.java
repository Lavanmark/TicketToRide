package com.floorcorn.tickettoride;

import com.floorcorn.tickettoride.clientModel.User;
import com.floorcorn.tickettoride.exceptions.BadUserException;
import com.floorcorn.tickettoride.exceptions.GameActionException;
import com.floorcorn.tickettoride.model.IGame;
import com.floorcorn.tickettoride.model.IUser;
import com.floorcorn.tickettoride.model.Player;

import org.junit.*;

import java.util.Set;

import static org.junit.Assert.*;

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
			IUser res = sp.register(goodUser);
			assertNotEquals(res, null);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			assertTrue(false);
		}
		goodUser = new User("tyler", "dragonman");
		IUser res = null;
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
	}

	@Test
	public void testGameOperations() {
		IUser login = null;
		try {
			login = sp.login(new User("tyler","dragonman"));
		} catch(BadUserException e) {
			e.printStackTrace();
			assertFalse(true);
		}
		//TODO bad params gives end of file from server.
		IGame game = null;
		try {
			game = sp.createGame(login, "Test", 3);
		} catch(BadUserException e) {
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
		Set<IGame> games = null;
		try {
			games = sp.getGames(login);
		} catch(BadUserException e) {
			e.printStackTrace();
			assertFalse(true);
		}
		assertNotEquals(games, null);
		assertEquals(games.size(), 1);
		int gameID = -1;
		for(IGame g : games) {
			assertEquals(g.getGameSize(), game.getGameSize());
			assertEquals(g.getName(), game.getName());
			assertEquals(g.getGameID(), game.getGameID());
			gameID = g.getGameID();
		}

		//
		//JOIN GAME
		//
		try {
			game = sp.joinGame(login, gameID, Player.PlayerColor.BLACK);
		} catch(BadUserException | GameActionException e) {
			e.printStackTrace();
			assertFalse(true);
		}
		assertNotEquals(game, null);
		assertEquals(game.getGameID(), gameID);
		assertFalse(game.hasStarted());
		assertFalse(game.isFinsihed());
		assertEquals(game.getPlayerList().size(), 1);
		for(Player p : game.getPlayerList()) {
			assertEquals(p.getGameID(), game.getGameID());
			assertEquals(p.getColor(), Player.PlayerColor.BLACK);
			assertEquals(p.getUserID(), login.getUserID());
			assertEquals(p.getPlayerID(), 0);
			assertTrue(p.isConductor());
		}

		assertTrue(game.isPlayer(login.getUserID()));
		try {
			assertFalse(game.userCanJoin(login));
		} catch(BadUserException | GameActionException e) {
			e.printStackTrace();
			assertFalse(true);
		}
		assertEquals(game.getPlayer(login).getUserID(), login.getUserID());
		assertEquals(game.getPlayer(login).getColor(), Player.PlayerColor.BLACK);

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
		for(IGame g : games) {
			assertEquals(g.getGameID(), game.getGameID());
			assertTrue(g.isFinsihed());
		}
	}
}
