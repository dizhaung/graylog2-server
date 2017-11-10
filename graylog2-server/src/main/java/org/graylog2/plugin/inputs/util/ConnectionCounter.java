/**
 * This file is part of Graylog.
 *
 * Graylog is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Graylog is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graylog.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.graylog2.plugin.inputs.util;

import com.codahale.metrics.Gauge;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ConnectionCounter extends ChannelInboundHandlerAdapter {
    private final AtomicInteger connections;
    private final AtomicLong totalConnections;

    public ConnectionCounter() {
        this.connections = new AtomicInteger();
        this.totalConnections = new AtomicLong();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        connections.incrementAndGet();
        totalConnections.incrementAndGet();
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        connections.decrementAndGet();
        super.channelInactive(ctx);
    }

    public int getConnectionCount() {
        return connections.get();
    }

    public long getTotalConnections() {
        return totalConnections.get();
    }

    public Gauge<Integer> gaugeCurrent() {
        return this::getConnectionCount;
    }

    public Gauge<Long> gaugeTotal() {
        return this::getTotalConnections;
    }
}
